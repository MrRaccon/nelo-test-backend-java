package mx.com.test.nelo.alexis.service;

import mx.com.test.nelo.alexis.dto.*;
import mx.com.test.nelo.alexis.model.*;
import mx.com.test.nelo.alexis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReservationService {
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Autowired
    private RestaurantTableRepository restaurantTableRepository;
    
    @Autowired
    private DinerRepository dinerRepository;
    
    @Autowired
    private DietaryRestrictionRepository dietaryRestrictionRepository;
    
    public ReservationResponse createReservation(ReservationRequest request) {
        // Validate restaurant exists and is open
        Restaurant restaurant = restaurantRepository.findById(request.getRestaurantId())
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));
        
        validateRestaurantOperatingHours(restaurant, request.getReservationTime());
        
        // Validate tables are available
        validateTableAvailability(request.getRestaurantId(), request.getTableIds(), 
                              request.getReservationTime());
        
        // Create reservation
        Reservation reservation = new Reservation(restaurant, request.getReservationTime());
        
        // Add tables to reservation
        for (Long tableId : request.getTableIds()) {
            RestaurantTable table = restaurantTableRepository.findById(tableId)
                    .orElseThrow(() -> new RuntimeException("Table not found: " + tableId));
            reservation.addTable(table);
        }
        
        // Create diners and add to reservation
        List<DinerDto> dinerDtos = new ArrayList<>();
        for (DinerDto dinerDto : request.getDiners()) {
            Diner diner = new Diner(dinerDto.getName());
            diner = dinerRepository.save(diner);
            
            // Add dietary restrictions
            if (dinerDto.getDietaryRestrictions() != null) {
                for (DietaryRestrictionType restrictionType : dinerDto.getDietaryRestrictions()) {
                    if (restrictionType != DietaryRestrictionType.NONE) {
                        DietaryRestriction restriction = new DietaryRestriction(restrictionType, diner);
                        dietaryRestrictionRepository.save(restriction);
                        diner.addDietaryRestriction(restriction);
                    }
                }
            }
            
            reservation.addDiner(diner);
            dinerDtos.add(new DinerDto(diner.getName(), 
                diner.getDietaryRestrictions().stream()
                    .map(DietaryRestriction::getRestrictionType)
                    .collect(Collectors.toList())));
        }
        
        // Save reservation
        reservation = reservationRepository.save(reservation);
        
        return new ReservationResponse(
            reservation.getId(),
            restaurant.getId(),
            restaurant.getName(),
            request.getTableIds(),
            dinerDtos,
            reservation.getReservationTime(),
            reservation.getEndTime(),
            reservation.getCreatedAt()
        );
    }
    
    private void validateRestaurantOperatingHours(Restaurant restaurant, LocalDateTime reservationTime) {
        LocalTime time = reservationTime.toLocalTime();
        if (time.isBefore(restaurant.getOpeningTime()) || 
            time.isAfter(restaurant.getClosingTime())) {
            throw new RuntimeException("Restaurant is not open at the requested time");
        }
    }
    
    private void validateTableAvailability(Long restaurantId, List<Long> tableIds, 
                                      LocalDateTime reservationTime) {
        LocalDateTime endTime = reservationTime.plusHours(2); // 2-hour duration
        
        List<Reservation> conflictingReservations = reservationRepository
                .findConflictingReservationsForTables(tableIds, reservationTime, endTime);
        
        if (!conflictingReservations.isEmpty()) {
            throw new RuntimeException("One or more tables are not available at the requested time");
        }
    }
}
