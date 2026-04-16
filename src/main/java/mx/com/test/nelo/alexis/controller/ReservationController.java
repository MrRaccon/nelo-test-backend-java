package mx.com.test.nelo.alexis.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import mx.com.test.nelo.alexis.dto.ReservationRequest;
import mx.com.test.nelo.alexis.dto.ReservationResponse;
import mx.com.test.nelo.alexis.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
@Tag(name = "Reservation Management", description = "APIs for creating and managing reservations")
public class ReservationController {
    
    @Autowired
    private ReservationService reservationService;
    
    @PostMapping
    @Operation(summary = "Create a reservation", 
               description = "Create a new reservation for a group of diners at a specific time")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Reservation created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid reservation request"),
        @ApiResponse(responseCode = "404", description = "Restaurant or table not found"),
        @ApiResponse(responseCode = "409", description = "Tables not available at requested time")
    })
    public ResponseEntity<ReservationResponse> createReservation(
            @Valid @RequestBody ReservationRequest request) {
        
        ReservationResponse response = reservationService.createReservation(request);
        return ResponseEntity.status(201).body(response);
    }
}
