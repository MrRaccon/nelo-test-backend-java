package mx.com.test.nelo.alexis.service;

import mx.com.test.nelo.alexis.dto.*;
import mx.com.test.nelo.alexis.model.*;
import mx.com.test.nelo.alexis.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class RestaurantSearchService {
    
    @Autowired
    private RestaurantRepository restaurantRepository;
    
    @Autowired
    private RestaurantTableRepository restaurantTableRepository;
    
    @Autowired
    private DietaryRestrictionRepository dietaryRestrictionRepository;
    
    public List<RestaurantSearchResponse> findAvailableRestaurants(RestaurantSearchBeanParam searchParams) {
        // Extract dietary restrictions from diners
        Set<DietaryRestrictionType> requiredRestrictions = extractDietaryRestrictions(searchParams.getDiners());
        
        // Get restaurants that are open and have required endorsements
        List<Restaurant> eligibleRestaurants = findEligibleRestaurants(
            searchParams.getReservationTime().toLocalTime(), 
            requiredRestrictions
        );
        
        // Find available table combinations for each restaurant
        List<RestaurantSearchResponse> responses = new ArrayList<>();
        for (Restaurant restaurant : eligibleRestaurants) {
            List<TableCombinationDto> availableCombinations = findAvailableTableCombinations(
                restaurant, 
                searchParams.getPartySize(), 
                searchParams.getReservationTime()
            );
            
            if (!availableCombinations.isEmpty()) {
                responses.add(new RestaurantSearchResponse(
                    restaurant.getId(),
                    restaurant.getName(),
                    availableCombinations
                ));
            }
        }
        
        return responses;
    }
    
    private Set<DietaryRestrictionType> extractDietaryRestrictions(List<DinerDto> diners) {
        return diners.stream()
                .flatMap(diner -> diner.getDietaryRestrictions().stream())
                .filter(restriction -> restriction != DietaryRestrictionType.NONE)
                .collect(Collectors.toSet());
    }
    
    private List<Restaurant> findEligibleRestaurants(LocalTime reservationTime, 
                                                   Set<DietaryRestrictionType> requiredRestrictions) {
        if (requiredRestrictions.isEmpty()) {
            // No dietary restrictions, just find open restaurants
            return restaurantRepository.findOpenRestaurantsAtTime(reservationTime);
        } else {
            // Find restaurants that are open AND have all required endorsements
            return restaurantRepository.findOpenRestaurantsWithEndorsementsAndCapacity(
                reservationTime,
                1, // minimum capacity
                1, // total capacity (will be filtered later)
                requiredRestrictions
            );
        }
    }
    
    private List<TableCombinationDto> findAvailableTableCombinations(Restaurant restaurant, 
                                                               Integer partySize, 
                                                               LocalDateTime reservationTime) {
        LocalDateTime endTime = reservationTime.plusHours(2); // 2-hour duration
        
        // Find all available tables for the time slot
        List<RestaurantTable> availableTables = restaurantTableRepository.findAllAvailableTables(
            restaurant.getId(),
            reservationTime,
            endTime
        );
        
        // Filter tables by capacity (show tables up to 2 more seats than party size)
        List<RestaurantTable> suitableTables = availableTables.stream()
                .filter(table -> table.getCapacity() >= partySize && 
                                  table.getCapacity() <= partySize + 2)
                .collect(Collectors.toList());
        
        // Find combinations of tables that can accommodate the party
        return findTableCombinations(suitableTables, partySize);
    }
    
    private List<TableCombinationDto> findTableCombinations(List<RestaurantTable> tables, Integer partySize) {
        List<TableCombinationDto> combinations = new ArrayList<>();
        
        // Single table combinations
        for (RestaurantTable table : tables) {
            if (table.getCapacity() >= partySize) {
                combinations.add(new TableCombinationDto(
                    Arrays.asList(table.getId()),
                    Arrays.asList(table.getCapacity()),
                    table.getCapacity()
                ));
            }
        }
        
        // Multi-table combinations for larger parties
        if (partySize > 4) { // Only consider combinations for parties larger than 4
            combinations.addAll(findMultiTableCombinations(tables, partySize));
        }
        
        return combinations;
    }
    
    private List<TableCombinationDto> findMultiTableCombinations(List<RestaurantTable> tables, Integer partySize) {
        List<TableCombinationDto> combinations = new ArrayList<>();
        
        // Try combinations of 2 tables
        for (int i = 0; i < tables.size(); i++) {
            for (int j = i + 1; j < tables.size(); j++) {
                RestaurantTable table1 = tables.get(i);
                RestaurantTable table2 = tables.get(j);
                int totalCapacity = table1.getCapacity() + table2.getCapacity();
                
                if (totalCapacity >= partySize && totalCapacity <= partySize + 4) { // Allow up to 4 extra seats for multi-table
                    combinations.add(new TableCombinationDto(
                        Arrays.asList(table1.getId(), table2.getId()),
                        Arrays.asList(table1.getCapacity(), table2.getCapacity()),
                        totalCapacity
                    ));
                }
            }
        }
        
        return combinations;
    }
}
