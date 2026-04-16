package mx.com.test.nelo.alexis.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import mx.com.test.nelo.alexis.model.DietaryRestrictionType;

import java.util.List;

public class DinerDto {
    
    @NotBlank(message = "Diner name is required")
    @Size(max = 100, message = "Diner name must not exceed 100 characters")
    private String name;
    
    private List<DietaryRestrictionType> dietaryRestrictions;
    
    // Constructors
    public DinerDto() {}
    
    public DinerDto(String name, List<DietaryRestrictionType> dietaryRestrictions) {
        this.name = name;
        this.dietaryRestrictions = dietaryRestrictions;
    }
    
    // Getters and Setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<DietaryRestrictionType> getDietaryRestrictions() {
        return dietaryRestrictions;
    }
    
    public void setDietaryRestrictions(List<DietaryRestrictionType> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }
}
