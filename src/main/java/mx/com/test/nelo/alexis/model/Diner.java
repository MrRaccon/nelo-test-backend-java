package mx.com.test.nelo.alexis.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "diners")
@EntityListeners(AuditingEntityListener.class)
public class Diner {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @OneToMany(mappedBy = "diner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<DietaryRestriction> dietaryRestrictions = new HashSet<>();
    
    @ManyToMany(mappedBy = "diners")
    private Set<Reservation> reservations = new HashSet<>();
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Diner() {}
    
    public Diner(String name) {
        this.name = name;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public Set<DietaryRestriction> getDietaryRestrictions() {
        return dietaryRestrictions;
    }
    
    public void setDietaryRestrictions(Set<DietaryRestriction> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }
    
    public Set<Reservation> getReservations() {
        return reservations;
    }
    
    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Helper methods
    public void addDietaryRestriction(DietaryRestriction restriction) {
        dietaryRestrictions.add(restriction);
        restriction.setDiner(this);
    }
    
    public void removeDietaryRestriction(DietaryRestriction restriction) {
        dietaryRestrictions.remove(restriction);
        restriction.setDiner(null);
    }
}
