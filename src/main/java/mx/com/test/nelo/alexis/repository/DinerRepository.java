package mx.com.test.nelo.alexis.repository;

import mx.com.test.nelo.alexis.model.Diner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DinerRepository extends JpaRepository<Diner, Long> {
    
    List<Diner> findByNameContainingIgnoreCase(String name);
    
    boolean existsByNameIgnoreCase(String name);
}
