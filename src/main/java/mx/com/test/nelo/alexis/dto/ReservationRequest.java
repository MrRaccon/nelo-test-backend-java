package mx.com.test.nelo.alexis.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationRequest {
    
    @NotNull(message = "Restaurant ID is required")
    private Long restaurantId;
    
    @NotEmpty(message = "Table IDs cannot be empty")
    private List<Long> tableIds;
    
    @NotEmpty(message = "Diners list cannot be empty")
    @Valid
    private List<DinerDto> diners;
    
    @NotNull(message = "Reservation time is required")
    private LocalDateTime reservationTime;
    
    // Constructors
    public ReservationRequest() {}
    
    public ReservationRequest(Long restaurantId, List<Long> tableIds, List<DinerDto> diners, LocalDateTime reservationTime) {
        this.restaurantId = restaurantId;
        this.tableIds = tableIds;
        this.diners = diners;
        this.reservationTime = reservationTime;
    }
    
    // Getters and Setters
    public Long getRestaurantId() {
        return restaurantId;
    }
    
    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
    
    public List<Long> getTableIds() {
        return tableIds;
    }
    
    public void setTableIds(List<Long> tableIds) {
        this.tableIds = tableIds;
    }
    
    public List<DinerDto> getDiners() {
        return diners;
    }
    
    public void setDiners(List<DinerDto> diners) {
        this.diners = diners;
    }
    
    public LocalDateTime getReservationTime() {
        return reservationTime;
    }
    
    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }
}
