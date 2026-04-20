# Nelo Restaurant Booking API

A comprehensive Social Restaurant Booking API that allows users to find restaurants matching dietary restrictions and create reservations for groups.

## Features

- **Restaurant Search**: Find restaurants with available tables that match all group dietary restrictions
- **Reservation Creation**: Book tables for specific time slots with 2-hour duration
- **Dietary Restriction Matching**: Strict matching - excludes restaurants if any restriction isn't supported
- **Smart Table Allocation**: Shows tables up to 2 seats more than party size, supports multi-table combinations
- **Operating Hours**: Respects restaurant opening/closing times
- **Back-to-back Reservations**: Allows consecutive 2-hour slots (6:30-8:30, then 8:30-10:30)

## Tech Stack

- **Java 17** + Spring Boot 3.2.5
- **Spring Data JPA** + Hibernate
- **H2 Database** (dev) / **MySQL** (production)
- **OpenAPI 3** + Swagger UI
- **Jakarta Validation**
- **Docker** + Docker Compose (optional)

## Quick Start

```bash
# Run locally
mvn spring-boot:run

# Or with Docker
docker-compose up -d
```

**Base URL:** `http://localhost:8080/api/v1/`  
**Swagger:** `http://localhost:8080/api/v1/swagger-ui.html`

## API Endpoints

### 📘 API Overview

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `GET` | `/api/v1/` | API information and available endpoints | No |
| `POST` | `/api/v1/restaurants/search` | Search restaurants with available tables | No |
| `POST` | `/api/v1/reservations` | Create a new reservation | No |

---

### 1. Search Available Restaurants

**POST** `/api/v1/restaurants/search`

Find restaurants with available tables that match all dietary restrictions of the group. This endpoint performs strict matching - if any diner has a dietary restriction that a restaurant doesn't support, that restaurant is excluded from results.

#### Request Body

```json
{
  "diners": [
    {
      "name": "Jack",
      "dietaryRestrictions": ["VEGAN"]
    },
    {
      "name": "Jill",
      "dietaryRestrictions": ["VEGETARIAN"]
    },
    {
      "name": "Jane"
    }
  ],
  "reservationTime": "2026-04-19T19:30:00",
  "partySize": 3
}
```

#### Request Fields Explained

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `diners` | Array | ✅ Yes | **List of all people in the dining party.** Each diner can have specific dietary needs. The API uses this to filter restaurants that can accommodate ALL restrictions. |
| `diners[].name` | String | ✅ Yes | **Diner's full name.** Used for identification in the reservation system. Maximum 100 characters. |
| `diners[].dietaryRestrictions` | Array | ❌ No | **List of dietary restrictions this specific diner has.** Valid values: `VEGAN`, `VEGETARIAN`, `PALEO`, `GLUTEN_FREE`, `DAIRY_FREE`, `NUT_FREE`, `KOSHER`, `HALAL`, `LOW_SODIUM`, `DIABETIC_FRIENDLY`, `KETO`, `LOW_CARB`, `RAW_FOOD`, `MEDITERRANEAN`. If omitted or empty, the diner has no restrictions. |
| `reservationTime` | DateTime | ✅ Yes | **When the group wants to dine.** Must be in ISO 8601 format (`YYYY-MM-DDTHH:MM:SS`). Used to check table availability during this 2-hour window and verify restaurant is open at this time. |
| `partySize` | Integer | ✅ Yes | **Total number of people in the group.** Must be ≥ 1. Used to filter tables with sufficient capacity (tables shown have capacity between `partySize` and `partySize + 2` for single tables). |

#### How It Works

1. **Collects all unique dietary restrictions** from all diners in the request
2. **Filters restaurants** that have endorsements for ALL those restrictions
3. **Checks operating hours** - restaurant must be open at the requested time
4. **Finds available tables** - tables not reserved during the requested 2-hour slot
5. **Filters by capacity** - returns tables that fit the party size (with +2 seat buffer)
6. **Generates combinations** - for larger parties (>4), suggests multiple table combinations

#### Response (200 OK)

```json
[
  {
    "restaurantId": 1,
    "restaurantName": "The Green Garden",
    "availableTableCombinations": [
      {
        "tableIds": [2],
        "tableCapacities": [4],
        "totalCapacity": 4
      },
      {
        "tableIds": [1, 3],
        "tableCapacities": [2, 2],
        "totalCapacity": 4
      }
    ]
  }
]
```

#### Response Fields Explained

| Field | Type | Description |
|-------|------|-------------|
| `restaurantId` | Long | **Unique identifier** of the restaurant. Use this ID when creating a reservation. |
| `restaurantName` | String | **Human-readable name** of the restaurant for display purposes. |
| `availableTableCombinations` | Array | **List of possible seating arrangements.** Each combination shows which tables can accommodate the party. |
| `availableTableCombinations[].tableIds` | Array | **Table identifiers** that form this seating option. Pass these IDs to the reservation endpoint. |
| `availableTableCombinations[].tableCapacities` | Array | **How many seats each table has.** Helps users understand seating layout (e.g., [2,2] = two tables of 2 seats each). |
| `availableTableCombinations[].totalCapacity` | Integer | **Sum of all table capacities** in this combination. Always ≥ partySize. |

#### Empty Response

If no restaurants match, returns empty array `[]`. Common causes:
- No restaurant supports all required dietary restrictions
- All suitable tables are reserved at the requested time
- Restaurant is closed at the requested time

#### Error Responses

| Status | Description | Example Cause |
|--------|-------------|---------------|
| `400 Bad Request` | Request validation failed | Missing `partySize`, invalid date format, empty `diners` array |
| `400 Bad Request` | Invalid dietary restriction | Unknown restriction type like `VEGGIE` instead of `VEGETARIAN` |

---

### 2. Create Reservation

**POST** `/api/v1/reservations`

Book a reservation for a group at a specific restaurant and time. Call this endpoint **after** using the search endpoint to find available restaurants and table combinations. The reservation locks the tables for exactly 2 hours.

#### Request Body

```json
{
  "restaurantId": 1,
  "tableIds": [2],
  "diners": [
    {
      "name": "Jack",
      "dietaryRestrictions": ["VEGAN"]
    },
    {
      "name": "Jill",
      "dietaryRestrictions": ["VEGETARIAN"]
    },
    {
      "name": "Jane"
    }
  ],
  "reservationTime": "2026-04-19T19:30:00"
}
```

#### Request Fields Explained

| Field | Type | Required | Description |
|-------|------|----------|-------------|
| `restaurantId` | Long | ✅ Yes | **Restaurant to book.** Must be a valid ID from the search endpoint results. This identifies which restaurant will host the reservation. |
| `tableIds` | Array | ✅ Yes | **Specific tables to reserve.** Use the `tableIds` from a combination in the search response. Must be available at the requested time or you'll get a 409 Conflict. Can reserve multiple tables for large parties. |
| `diners` | Array | ✅ Yes | **Complete list of diners.** Same structure as search endpoint. Stored with the reservation for reference. The API verifies the restaurant still supports all dietary restrictions at booking time. |
| `diners[].name` | String | ✅ Yes | **Diner's name.** Displayed on the reservation and potentially used by restaurant staff. |
| `diners[].dietaryRestrictions` | Array | ❌ No | **Diner's dietary needs.** Optional. If provided, the API double-checks the restaurant can accommodate them (prevents race conditions where endorsements changed between search and booking). |
| `reservationTime` | DateTime | ✅ Yes | **When the reservation starts.** Must match a time that showed available tables in the search. ISO 8601 format. The reservation lasts 2 hours from this time (e.g., 19:30 → 21:30). |

#### Workflow

1. **Search** → Find restaurants and available table combinations for your party
2. **Select** → Choose a restaurant and table combination from search results
3. **Book** → Call this endpoint with the chosen `restaurantId` and `tableIds`
4. **Confirm** → Receive reservation confirmation with unique `reservationId`

#### Response (201 Created)

```json
{
  "reservationId": 123,
  "restaurantId": 1,
  "restaurantName": "The Green Garden",
  "tableIds": [2],
  "diners": [
    {
      "name": "Jack",
      "dietaryRestrictions": ["VEGAN"]
    },
    {
      "name": "Jill",
      "dietaryRestrictions": ["VEGETARIAN"]
    },
    {
      "name": "Jane",
      "dietaryRestrictions": []
    }
  ],
  "reservationTime": "2026-04-19T19:30:00",
  "endTime": "2026-04-19T21:30:00",
  "createdAt": "2026-04-19T18:45:00"
}
```

#### Response Fields Explained

| Field | Type | Description |
|-------|------|-------------|
| `reservationId` | Long | **Unique booking identifier.** Store this for future reference (modifications, cancellations, inquiries). This is the confirmation number. |
| `restaurantId` | Long | **Confirmed restaurant ID.** Echoes back the restaurant you booked. |
| `restaurantName` | String | **Restaurant name** for display in confirmation emails/apps. |
| `tableIds` | Array | **Confirmed table IDs.** Echoes back which tables were reserved. These tables are now locked for your 2-hour window. |
| `diners` | Array | **Confirmed diner list.** Echoes back with all dietary restrictions preserved. Used by restaurant kitchen staff to prepare appropriate meals. |
| `reservationTime` | DateTime | **Start time** of your reservation. Arrive at or shortly after this time. |
| `endTime` | DateTime | **End time** calculated as start + 2 hours. Your table is guaranteed until this time. |
| `createdAt` | DateTime | **When the booking was made.** Timestamp of successful reservation creation. |

#### Error Responses

| Status | Description | Cause | Resolution |
|--------|-------------|-------|------------|
| `400 Bad Request` | Validation error | Missing required fields, invalid `restaurantId` format, empty `tableIds`, invalid date format | Check all required fields are present and properly formatted |
| `404 Not Found` | Resource not found | `restaurantId` doesn't exist, `tableIds` don't exist or don't belong to this restaurant | Verify IDs from search response are used correctly |
| `409 Conflict` | Tables unavailable | One or more tables in `tableIds` are already reserved for overlapping time slot | Search again for the same time - another user booked between your search and booking |
| `409 Conflict` | Restaurant closed | `reservationTime` is outside restaurant operating hours | Check restaurant opening hours and adjust time |
| `400 Bad Request` | Dietary mismatch | Restaurant no longer supports a dietary restriction (rare race condition) | Search again to get updated restaurant list |

---

### 3. API Information

**GET** `/api/v1/`

Returns basic information about the API and available endpoints.

#### Response (200 OK)

```json
{
  "application": "Nelo Restaurant Booking API",
  "version": "1.0.0",
  "description": "Social Restaurant Booking API for finding restaurants with dietary restrictions and making reservations",
  "endpoints": {
    "search": "POST /api/v1/restaurants/search - Search available restaurants",
    "reserve": "POST /api/v1/reservations - Create reservation",
    "docs": "/api/v1/swagger-ui.html - API Documentation",
    "h2-console": "/api/v1/h2-console - Database Console"
  }
}
```

## Business Logic

### Dietary Restriction Matching
- **Strict Matching**: If any diner's restriction isn't supported by restaurant, restaurant is excluded
- **Supported Types**: VEGAN, VEGETARIAN, PALEO, GLUTEN_FREE, DAIRY_FREE, NUT_FREE, KOSHER, HALAL, LOW_SODIUM, DIABETIC_FRIENDLY, KETO, LOW_CARB, RAW_FOOD, MEDITERRANEAN

### Table Capacity Rules
- **Single Tables**: Show tables with capacity ≥ party size and ≤ party size + 2
- **Multi-table Combinations**: For parties > 4, allow combining tables (up to party size + 4 total capacity)
- **Example**: Party of 3 → Tables for 3-5 people, Party of 8 → Multiple 4-person tables

### Time Slot Management
- **Fixed Duration**: All reservations last exactly 2 hours
- **Back-to-back**: End time of one reservation can be start time of next
- **Operating Hours**: Reservations only allowed within restaurant operating hours

### Diner Management
- **On-the-fly Creation**: Diners created during reservation (no persistent profiles)
- **Minimal Data**: Only name and dietary restrictions stored
- **Privacy**: Diner data isolated per reservation

---

## Database Schema

### Core Entities
- **Restaurant**: Name, operating hours, relationships to tables/endorsements/reservations
- **RestaurantTable**: Capacity, restaurant relationship, reservation availability
- **RestaurantEndorsement**: Dietary restriction endorsements (VEGAN-friendly, etc.)
- **Reservation**: Restaurant, tables, diners, time slot (2-hour duration)
- **Diner**: Name, dietary restrictions, reservations
- **DietaryRestriction**: Restriction type per diner

### Relationships
- Restaurant → Tables (1:N)
- Restaurant → Endorsements (1:N)
- Restaurant → Reservations (1:N)
- Reservation → Tables (N:M) - supports multi-table reservations
- Reservation → Diners (N:M)
- Diner → DietaryRestrictions (1:N)

## Sample Data

The application includes sample data with:
- 5 restaurants with different operating hours
- Various dietary endorsements per restaurant
- Multiple table capacities (2, 4, 6, 8 seats)
- Ready for immediate testing

## Error Handling

The API provides comprehensive error responses:
- **400 Bad Request**: Validation errors, invalid requests
- **404 Not Found**: Restaurant/table not found
- **409 Conflict**: Tables not available at requested time
- **500 Internal Server Error**: Unexpected server errors

All errors include timestamp, status, error type, message, and validation details.

## Production Considerations

- **Database**: Switch to MySQL by updating application.properties
- **Security**: Add authentication/authorization as needed
- **Scaling**: Consider connection pooling and caching for high load
- **Monitoring**: Leverage Spring Boot Actuator endpoints
- **Deployment**: Package as JAR or deploy to container platform

## License

This project is part of Nelo's technical assessment process.
