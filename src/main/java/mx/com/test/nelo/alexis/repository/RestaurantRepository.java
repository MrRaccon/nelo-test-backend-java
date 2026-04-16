package mx.com.test.nelo.alexis.repository;

import mx.com.test.nelo.alexis.model.DietaryRestrictionType;
import mx.com.test.nelo.alexis.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    
    @Query("SELECT r FROM Restaurant r WHERE r.openingTime <= :time AND r.closingTime > :time")
    List<Restaurant> findOpenRestaurantsAtTime(@Param("time") LocalTime time);
    
    @Query("SELECT DISTINCT r FROM Restaurant r " +
           "JOIN r.endorsements e " +
           "WHERE e.endorsementType IN :restrictionTypes")
    List<Restaurant> findRestaurantsWithEndorsements(@Param("restrictionTypes") Set<DietaryRestrictionType> restrictionTypes);
    
    @Query("SELECT DISTINCT r FROM Restaurant r " +
           "JOIN r.endorsements e " +
           "WHERE r.openingTime <= :time AND r.closingTime > :time " +
           "AND e.endorsementType IN :restrictionTypes")
    List<Restaurant> findOpenRestaurantsWithEndorsements(
            @Param("time") LocalTime time, 
            @Param("restrictionTypes") Set<DietaryRestrictionType> restrictionTypes);
    
    @Query("SELECT r FROM Restaurant r " +
           "JOIN r.tables t " +
           "WHERE r.openingTime <= :time AND r.closingTime > :time " +
           "AND t.capacity >= :minCapacity " +
           "GROUP BY r " +
           "HAVING SUM(t.capacity) >= :totalCapacity")
    List<Restaurant> findRestaurantsWithSufficientCapacity(
            @Param("time") LocalTime time,
            @Param("minCapacity") Integer minCapacity,
            @Param("totalCapacity") Integer totalCapacity);
    
    @Query("SELECT DISTINCT r FROM Restaurant r " +
           "JOIN r.tables t " +
           "JOIN r.endorsements e " +
           "WHERE r.openingTime <= :time AND r.closingTime > :time " +
           "AND t.capacity >= :minCapacity " +
           "AND e.endorsementType IN :restrictionTypes " +
           "GROUP BY r " +
           "HAVING SUM(t.capacity) >= :totalCapacity")
    List<Restaurant> findOpenRestaurantsWithEndorsementsAndCapacity(
            @Param("time") LocalTime time,
            @Param("minCapacity") Integer minCapacity,
            @Param("totalCapacity") Integer totalCapacity,
            @Param("restrictionTypes") Set<DietaryRestrictionType> restrictionTypes);
}
