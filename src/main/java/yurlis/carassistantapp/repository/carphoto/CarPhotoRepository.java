package yurlis.carassistantapp.repository.carphoto;

import org.springframework.data.jpa.repository.JpaRepository;
import yurlis.carassistantapp.model.Car;
import yurlis.carassistantapp.model.CarPhoto;

import java.util.List;

public interface CarPhotoRepository extends JpaRepository<CarPhoto, Long> {
    List<CarPhoto> findByCarIdAndIsDeletedFalse(Long carId);
}
