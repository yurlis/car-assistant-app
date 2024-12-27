package yurlis.carassistantapp.repository.fueltype;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yurlis.carassistantapp.model.FuelType;

@Repository
public interface FuelTypeRepository extends JpaRepository<FuelType, Long> {
}
