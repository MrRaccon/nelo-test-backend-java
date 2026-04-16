package mx.com.test.nelo.alexis.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ReservationResponse {
    
    private Long reservationId;
    private Long restaurantId;
    private String restaurantName;
    private List<Long> tableIds;
    private List<DinerDto> diners;
    private LocalDateTime reservationTime;
    private LocalDateTime endTime;
    private LocalDateTime createdAt;
    
    // Constructors
    public ReservationResponse() {}
    
    public ReservationResponse(Long reservationId, Long restaurantId, String restaurantName, 
                          List<Long> tableIds, List<DinerDto> diners, 
                          LocalDateTime reservationTime, LocalDateTime endTime, LocalDateTime createdAt) {
        this.reservationId = reservationId;
        this.restaurantId = restaurantId;
        this.restaurantName = restaurantName;
        this.tableIds = tableIds;
        this.diners = diners;
        this.reservationTime = reservationTime;
        this.endTime = endTime;
        this.createdAt = createdAt;
    }
    
    // Getters and Setters
    public Long getReservationId() {
        return reservationId;
    }
    
    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
    
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
    
    public LocalDateTime getEndTime() {
        return endTime;
    }
    
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
