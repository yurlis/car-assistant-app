package yurlis.carassistantapp.service.car;

import yurlis.carassistantapp.dto.car.CarPhotoResponseDto;
import yurlis.carassistantapp.model.CarPhoto;

import java.util.List;

public interface CarPhotoService {
    List<CarPhotoResponseDto> getAllPhotosForCar(Long carId, Long userId);
    void deleteCarPhotosFromCloud(List<CarPhoto> carPhotos);
    void deleteCarPhotoFromCloud(CarPhoto carPhoto);
}
