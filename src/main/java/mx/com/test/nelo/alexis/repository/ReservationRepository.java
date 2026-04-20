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
    
    @Query(value = "SELECT * FROM reservations r " +
           "WHERE r.restaurant_id = :restaurantId " +
           "AND ((" +
           "  r.reservation_time <= :startTime AND DATE_ADD(r.reservation_time, INTERVAL r.duration_hours HOUR) > :startTime" +
           ") OR (" +
           "  r.reservation_time < :endTime AND DATE_ADD(r.reservation_time, INTERVAL r.duration_hours HOUR) >= :endTime" +
           ") OR (" +
           "  r.reservation_time >= :startTime AND r.reservation_time < :endTime" +
           "))", nativeQuery = true)
    List<Reservation> findConflictingReservations(
            @Param("restaurantId") Long restaurantId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    @Query(value = "SELECT DISTINCT r.* FROM reservations r " +
           "JOIN reservation_tables rt ON r.id = rt.reservation_id " +
           "WHERE rt.table_id IN :tableIds " +
           "AND ((" +
           "  r.reservation_time <= :startTime AND DATE_ADD(r.reservation_time, INTERVAL r.duration_hours HOUR) > :startTime" +
           ") OR (" +
           "  r.reservation_time < :endTime AND DATE_ADD(r.reservation_time, INTERVAL r.duration_hours HOUR) >= :endTime" +
           ") OR (" +
           "  r.reservation_time >= :startTime AND r.reservation_time < :endTime" +
           "))", nativeQuery = true)
    List<Reservation> findConflictingReservationsForTables(
            @Param("tableIds") List<Long> tableIds,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    List<Reservation> findByRestaurantIdOrderByReservationTimeDesc(Long restaurantId);
}
