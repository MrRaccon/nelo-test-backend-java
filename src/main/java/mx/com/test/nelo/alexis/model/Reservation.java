package mx.com.test.nelo.alexis.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "reservations")
@EntityListeners(AuditingEntityListener.class)
public class Reservation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    private Restaurant restaurant;
    
    @ManyToMany
    @JoinTable(
        name = "reservation_tables",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "table_id")
    )
    private Set<RestaurantTable> tables = new HashSet<>();
    
    @ManyToMany
    @JoinTable(
        name = "reservation_diners",
        joinColumns = @JoinColumn(name = "reservation_id"),
        inverseJoinColumns = @JoinColumn(name = "diner_id")
    )
    private Set<Diner> diners = new HashSet<>();
    
    @Column(name = "reservation_time", nullable = false)
    private LocalDateTime reservationTime;
    
    @Column(name = "duration_hours", nullable = false)
    private Integer durationHours = 2; // Fixed 2-hour duration
    
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Constructors
    public Reservation() {}
    
    public Reservation(Restaurant restaurant, LocalDateTime reservationTime) {
        this.restaurant = restaurant;
        this.reservationTime = reservationTime;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Restaurant getRestaurant() {
        return restaurant;
    }
    
    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
    
    public Set<RestaurantTable> getTables() {
        return tables;
    }
    
    public void setTables(Set<RestaurantTable> tables) {
        this.tables = tables;
    }
    
    public Set<Diner> getDiners() {
        return diners;
    }
    
    public void setDiners(Set<Diner> diners) {
        this.diners = diners;
    }
    
    public LocalDateTime getReservationTime() {
        return reservationTime;
    }
    
    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }
    
    public Integer getDurationHours() {
        return durationHours;
    }
    
    public void setDurationHours(Integer durationHours) {
        this.durationHours = durationHours;
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
        table.getReservations().add(this);
    }
    
    public void removeTable(RestaurantTable table) {
        tables.remove(table);
        table.getReservations().remove(this);
    }
    
    public void addDiner(Diner diner) {
        diners.add(diner);
        diner.getReservations().add(this);
    }
    
    public void removeDiner(Diner diner) {
        diners.remove(diner);
        diner.getReservations().remove(this);
    }
    
    public LocalDateTime getEndTime() {
        return reservationTime.plusHours(durationHours);
    }
}
