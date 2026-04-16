package mx.com.test.nelo.alexis.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "restaurants")
@EntityListeners(AuditingEntityListener.class)
public class Restaurant {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String name;
    
    @Column(nullable = false)
    private LocalTime openingTime;
    
    @Column(nullable = false)
    private LocalTime closingTime;
    
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RestaurantTable> tables = new HashSet<>();
    
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RestaurantEndorsement> endorsements = new HashSet<>();
    
    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Reservation> reservations = new HashSet<>();
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Restaurant() {}
    
    public Restaurant(String name, LocalTime openingTime, LocalTime closingTime) {
        this.name = name;
        this.openingTime = openingTime;
        this.closingTime = closingTime;
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
    
    public LocalTime getOpeningTime() {
        return openingTime;
    }
    
    public void setOpeningTime(LocalTime openingTime) {
        this.openingTime = openingTime;
    }
    
    public LocalTime getClosingTime() {
        return closingTime;
    }
    
    public void setClosingTime(LocalTime closingTime) {
        this.closingTime = closingTime;
    }
    
    public Set<RestaurantTable> getTables() {
        return tables;
    }
    
    public void setTables(Set<RestaurantTable> tables) {
        this.tables = tables;
    }
    
    public Set<RestaurantEndorsement> getEndorsements() {
        return endorsements;
    }
    
    public void setEndorsements(Set<RestaurantEndorsement> endorsements) {
        this.endorsements = endorsements;
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
    public void addTable(RestaurantTable table) {
        tables.add(table);
        table.setRestaurant(this);
    }
    
    public void removeTable(RestaurantTable table) {
        tables.remove(table);
        table.setRestaurant(null);
    }
    
    public void addEndorsement(RestaurantEndorsement endorsement) {
        endorsements.add(endorsement);
        endorsement.setRestaurant(this);
    }
    
    public void removeEndorsement(RestaurantEndorsement endorsement) {
        endorsements.remove(endorsement);
        endorsement.setRestaurant(null);
    }
}
