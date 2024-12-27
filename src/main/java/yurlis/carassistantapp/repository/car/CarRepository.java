package yurlis.carassistantapp.repository.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import yurlis.carassistantapp.model.Car;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    @Query("SELECT c FROM Car c LEFT JOIN FETCH c.fuelTypes WHERE c.user.id = :userId")
    List<Car> findByUserId(Long userId);
}
