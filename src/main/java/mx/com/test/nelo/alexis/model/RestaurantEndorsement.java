package mx.com.test.nelo.alexis.model;

import jakarta.persistence.*;

@Entity
@Table(name = "restaurant_endorsements")
public class RestaurantEndorsement {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "endorsement_type", nullable = false)
    private DietaryRestrictionType endorsementType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    
    // Constructors
    public RestaurantEndorsement() {}
    
    public RestaurantEndorsement(DietaryRestrictionType endorsementType, Restaurant restaurant) {
        this.endorsementType = endorsementType;
        this.restaurant = restaurant;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public DietaryRestrictionType getEndorsementType() {
        return endorsementType;
    }
    
    public void setEndorsementType(DietaryRestrictionType endorsementType) {
        this.endorsementType = endorsementType;
    }
    
    public Restaurant getRestaurant() {
        return restaurant;
    }
    
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
