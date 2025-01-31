package yurlis.carassistantapp.repository.carrefuel;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yurlis.carassistantapp.model.CarRefuel;

import java.sql.Timestamp;

@Repository
public interface CarRefuelRepository extends JpaRepository<CarRefuel, Long> {

    Page<CarRefuel> findAllByCarId(Long carId, Pageable pageable);

    Page<CarRefuel> findAllByCarIdAndRefuelTimeBetween(Long carId, Timestamp startDate, Timestamp endDate, Pageable pageable);

    Page<CarRefuel> findAllByCarIdAndFuelTypeId(Long carId, Long fuelTypeId, Pageable pageable);

    Page<CarRefuel> findAllByCarIdAndFuelTypeIdAndRefuelTimeBetween(Long carId, Long fuelTypeId, Timestamp startDate, Timestamp endDate, Pageable pageable);
}
