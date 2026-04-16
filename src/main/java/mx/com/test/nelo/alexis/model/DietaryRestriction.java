package mx.com.test.nelo.alexis.model;

import jakarta.persistence.*;

@Entity
@Table(name = "dietary_restrictions")
public class DietaryRestriction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "restriction_type", nullable = false)
    private DietaryRestrictionType restrictionType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diner_id", nullable = false)
    private Diner diner;
    
    // Constructors
    public DietaryRestriction() {}
    
    public DietaryRestriction(DietaryRestrictionType restrictionType, Diner diner) {
        this.restrictionType = restrictionType;
        this.diner = diner;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public DietaryRestrictionType getRestrictionType() {
        return restrictionType;
    }
    
    public void setRestrictionType(DietaryRestrictionType restrictionType) {
        this.restrictionType = restrictionType;
    }
    
    public Diner getDiner() {
        return diner;
    }
    
    public void setDiner(Diner diner) {
        this.diner = diner;
    }
}
