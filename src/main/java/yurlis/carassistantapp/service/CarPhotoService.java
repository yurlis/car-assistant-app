package yurlis.carassistantapp.service;

import yurlis.carassistantapp.dto.car.CarPhotoResponseDto;
import yurlis.carassistantapp.model.CarPhoto;

import java.util.List;

public interface CarPhotoService {
    List<CarPhotoResponseDto> getAllPhotosForCar(Long carId);
    void deleteCarPhotosFromCloud(List<CarPhoto> carPhotos);
    void deleteCarPhotoFromCloud(CarPhoto carPhoto);
}
