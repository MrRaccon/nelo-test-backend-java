package mx.com.test.nelo.alexis.repository;

import mx.com.test.nelo.alexis.model.RestaurantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
    
    @Query("SELECT t FROM RestaurantTable t " +
           "WHERE t.restaurant.id = :restaurantId " +
           "AND t.capacity >= :minCapacity " +
           "AND t.id NOT IN (" +
           "  SELECT rt.id FROM Reservation r " +
           "  JOIN r.tables rt " +
           "  WHERE r.restaurant.id = :restaurantId " +
           "  AND ((" +
           "    r.reservationTime <= :startTime AND r.reservationTime.plusHours(r.durationHours) > :startTime" +
           "  ) OR (" +
           "    r.reservationTime < :endTime AND r.reservationTime.plusHours(r.durationHours) >= :endTime" +
           "  ) OR (" +
           "    r.reservationTime >= :startTime AND r.reservationTime < :endTime" +
           "  ))" +
           ")")
    List<RestaurantTable> findAvailableTables(
            @Param("restaurantId") Long restaurantId,
            @Param("minCapacity") Integer minCapacity,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT t FROM RestaurantTable t " +
           "WHERE t.restaurant.id = :restaurantId " +
           "AND t.id NOT IN (" +
           "  SELECT rt.id FROM Reservation r " +
           "  JOIN r.tables rt " +
           "  WHERE r.restaurant.id = :restaurantId " +
           "  AND ((" +
           "    r.reservationTime <= :startTime AND r.reservationTime.plusHours(r.durationHours) > :startTime" +
           "  ) OR (" +
           "    r.reservationTime < :endTime AND r.reservationTime.plusHours(r.durationHours) >= :endTime" +
           "  ) OR (" +
           "    r.reservationTime >= :startTime AND r.reservationTime < :endTime" +
           "  ))" +
           ")")
    List<RestaurantTable> findAllAvailableTables(
            @Param("restaurantId") Long restaurantId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
