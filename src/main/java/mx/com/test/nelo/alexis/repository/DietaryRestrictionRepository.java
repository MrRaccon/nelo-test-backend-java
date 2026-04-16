package mx.com.test.nelo.alexis.repository;

import mx.com.test.nelo.alexis.model.DietaryRestriction;
import mx.com.test.nelo.alexis.model.DietaryRestrictionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietaryRestrictionRepository extends JpaRepository<DietaryRestriction, Long> {
    
    List<DietaryRestriction> findByDinerId(Long dinerId);
    
    @Query("SELECT DISTINCT dr.restrictionType FROM DietaryRestriction dr WHERE dr.diner.id IN :dinerIds")
    List<DietaryRestrictionType> findRestrictionTypesByDinerIds(@Param("dinerIds") List<Long> dinerIds);
}
