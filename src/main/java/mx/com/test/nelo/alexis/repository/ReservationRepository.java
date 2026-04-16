package mx.com.test.nelo.alexis.repository;

import mx.com.test.nelo.alexis.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    
    @Query("SELECT r FROM Reservation r " +
           "WHERE r.restaurant.id = :restaurantId " +
           "AND ((" +
           "  r.reservationTime <= :startTime AND r.reservationTime.plusHours(r.durationHours) > :startTime" +
           ") OR (" +
           "  r.reservationTime < :endTime AND r.reservationTime.plusHours(r.durationHours) >= :endTime" +
           ") OR (" +
           "  r.reservationTime >= :startTime AND r.reservationTime < :endTime" +
           "))")
    List<Reservation> findConflictingReservations(
            @Param("restaurantId") Long restaurantId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT r FROM Reservation r " +
           "JOIN r.tables t " +
           "WHERE t.id IN :tableIds " +
           "AND ((" +
           "  r.reservationTime <= :startTime AND r.reservationTime.plusHours(r.durationHours) > :startTime" +
           ") OR (" +
           "  r.reservationTime < :endTime AND r.reservationTime.plusHours(r.durationHours) >= :endTime" +
           ") OR (" +
           "  r.reservationTime >= :startTime AND r.reservationTime < :endTime" +
           "))")
    List<Reservation> findConflictingReservationsForTables(
            @Param("tableIds") List<Long> tableIds,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    List<Reservation> findByRestaurantIdOrderByReservationTimeDesc(Long restaurantId);
}
