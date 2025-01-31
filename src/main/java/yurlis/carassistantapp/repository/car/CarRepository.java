package yurlis.carassistantapp.repository.car;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import yurlis.carassistantapp.model.Car;

import java.util.List;
import java.util.Optional;

public interface CarRepository extends JpaRepository<Car, Long> {
    // @Query("SELECT c FROM Car c LEFT JOIN FETCH c.fuelTypes WHERE c.user.id = :userId") // 1 v
    @EntityGraph(attributePaths = "fuelTypes")  // 2 v
    List<Car> findByUserId(Long userId);
    Optional<Car> findByIdAndUserId(Long id, Long userId);
    boolean existsByVinCode(String vinCode);
}
