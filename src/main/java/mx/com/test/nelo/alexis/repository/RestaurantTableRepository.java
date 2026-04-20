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
    
    @Query(value = "SELECT t.* FROM restaurant_tables t " +
           "WHERE t.restaurant_id = :restaurantId " +
           "AND t.capacity >= :minCapacity " +
           "AND t.id NOT IN (" +
           "  SELECT rt.table_id FROM reservation_tables rt " +
           "  JOIN reservations r ON rt.reservation_id = r.id " +
           "  WHERE r.restaurant_id = :restaurantId " +
           "  AND ((" +
           "    r.reservation_time <= :startTime AND DATE_ADD(r.reservation_time, INTERVAL r.duration_hours HOUR) > :startTime" +
           "  ) OR (" +
           "    r.reservation_time < :endTime AND DATE_ADD(r.reservation_time, INTERVAL r.duration_hours HOUR) >= :endTime" +
           "  ) OR (" +
           "    r.reservation_time >= :startTime AND r.reservation_time < :endTime" +
           "  ))" +
           ")", nativeQuery = true)
    List<RestaurantTable> findAvailableTables(
            @Param("restaurantId") Long restaurantId,
            @Param("minCapacity") Integer minCapacity,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    @Query(value = "SELECT t.* FROM restaurant_tables t " +
           "WHERE t.restaurant_id = :restaurantId " +
           "AND t.id NOT IN (" +
           "  SELECT rt.table_id FROM reservation_tables rt " +
           "  JOIN reservations r ON rt.reservation_id = r.id " +
           "  WHERE r.restaurant_id = :restaurantId " +
           "  AND ((" +
           "    r.reservation_time <= :startTime AND DATE_ADD(r.reservation_time, INTERVAL r.duration_hours HOUR) > :startTime" +
           "  ) OR (" +
           "    r.reservation_time < :endTime AND DATE_ADD(r.reservation_time, INTERVAL r.duration_hours HOUR) >= :endTime" +
           "  ) OR (" +
           "    r.reservation_time >= :startTime AND r.reservation_time < :endTime" +
           "  ))" +
           ")", nativeQuery = true)
    List<RestaurantTable> findAllAvailableTables(
            @Param("restaurantId") Long restaurantId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
}
