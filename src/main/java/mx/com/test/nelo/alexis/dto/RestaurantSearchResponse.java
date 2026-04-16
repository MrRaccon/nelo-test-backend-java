package mx.com.test.nelo.alexis.dto;

import java.util.List;

public class RestaurantSearchResponse {
    
    private Long restaurantId;
    private String restaurantName;
    private List<TableCombinationDto> availableTableCombinations;
    
    // Constructors
    public RestaurantSearchResponse() {}
    
    public RestaurantSearchResponse(Long restaurantId, String restaurantName, List<TableCombinationDto> availableTableCombinations) {
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.availableTableCombinations = availableTableCombinations;
    }
    
    // Getters and Setters
    public Long getRestaurantId() {
        return restaurantId;
    }
    
    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
    
    public String getRestaurantName() {
        return restaurantName;
    }
    
    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
    
    public List<TableCombinationDto> getAvailableTableCombinations() {
        return availableTableCombinations;
    }
    
    public void setAvailableTableCombinations(List<TableCombinationDto> availableTableCombinations) {
        this.availableTableCombinations = availableTableCombinations;
    }
}
