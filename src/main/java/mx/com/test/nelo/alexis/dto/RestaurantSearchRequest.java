package mx.com.test.nelo.alexis.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.List;

public class RestaurantSearchRequest {
    
    @NotEmpty(message = "Diners list cannot be empty")
    @Valid
    private List<DinerDto> diners;
    
    @NotNull(message = "Reservation time is required")
    private LocalDateTime reservationTime;
    
    @Positive(message = "Party size must be positive")
    private Integer partySize;
    
    // Constructors
    public RestaurantSearchRequest() {}
    
    public RestaurantSearchRequest(List<DinerDto> diners, LocalDateTime reservationTime, Integer partySize) {
        this.diners = diners;
        this.reservationTime = reservationTime;
        this.partySize = partySize;
    }
    
    // Getters and Setters
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
    
    public Integer getPartySize() {
        return partySize;
    }
    
    public void setPartySize(Integer partySize) {
        this.partySize = partySize;
    }
}
