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

- **Java 17** with Spring Boot 3.2.5
- **Spring Data JPA** with Hibernate
- **H2 Database** for development (MySQL support included)
- **OpenAPI 3** with Swagger UI
- **Jakarta Validation** for request validation
- **Spring Boot Actuator** for health monitoring

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+

### Running the Application

```bash
# Clone the repository
git clone <repository-url>
cd nelo-test-backend-java

# Build and run
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access Points

- **API Root**: http://localhost:8080/api/v1/
- **Swagger Documentation**: http://localhost:8080/api/v1/swagger-ui.html
- **H2 Database Console**: http://localhost:8080/api/v1/h2-console
- **Health Check**: http://localhost:8080/api/v1/actuator/health

## API Endpoints

### 1. Search Available Restaurants

**GET** `/api/v1/restaurants/search`

Find restaurants with available tables that match dietary restrictions.

**Query Parameters:**
```
?diners[0].name=Jack&diners[0].dietaryRestrictions=VEGAN
&diners[1].name=Jill&diners[1].dietaryRestrictions=VEGETARIAN
&diners[2].name=Jane&diners[2].dietaryRestrictions=NONE
&reservationTime=2024-04-16T19:30:00
&partySize=3
```

**Example URL:**
```
GET /api/v1/restaurants/search?diners[0].name=Jack&diners[0].dietaryRestrictions=VEGAN&diners[1].name=Jill&diners[1].dietaryRestrictions=VEGETARIAN&diners[2].name=Jane&diners[2].dietaryRestrictions=NONE&reservationTime=2024-04-16T19:30:00&partySize=3
```

**Response:**
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

### 2. Create Reservation

**POST** `/api/v1/reservations`

Create a reservation for a group at a specific restaurant and time.

**Request Body:**
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
      "name": "Jane",
      "dietaryRestrictions": []
    }
  ],
  "reservationTime": "2024-04-16T19:30:00"
}
```

**Response:**
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
    }
  ],
  "reservationTime": "2024-04-16T19:30:00",
  "endTime": "2024-04-16T21:30:00",
  "createdAt": "2024-04-16T18:45:00"
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

## Development

### Database Console
Access H2 console at: http://localhost:8080/api/v1/h2-console
- **JDBC URL**: `jdbc:h2:mem:restaurantdb`
- **Username**: `sa`
- **Password**: `password`

### Testing
Use Swagger UI at http://localhost:8080/api/v1/swagger-ui.html for interactive API testing.

## Production Considerations

- **Database**: Switch to MySQL by updating application.properties
- **Security**: Add authentication/authorization as needed
- **Scaling**: Consider connection pooling and caching for high load
- **Monitoring**: Leverage Spring Boot Actuator endpoints
- **Deployment**: Package as JAR or deploy to container platform

## License

This project is part of Nelo's technical assessment process.
