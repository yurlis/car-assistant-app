package yurlis.carassistantapp.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yurlis.carassistantapp.dto.car.CarPhotoResponseDto;
import yurlis.carassistantapp.mapper.CarPhotoMapper;
import yurlis.carassistantapp.model.Car;
import yurlis.carassistantapp.model.CarPhoto;
import yurlis.carassistantapp.repository.car.CarRepository;
import yurlis.carassistantapp.repository.carphoto.CarPhotoRepository;
import yurlis.carassistantapp.service.photoservice.PhotoStorageService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarPhotoServiceImpl implements CarPhotoService {

    private final PhotoStorageService photoStorageService;
    private final CarRepository carRepository;
    private final CarPhotoRepository carPhotoRepository;
    private final CarPhotoMapper carPhotoMapper;

    @Override
    public List<CarPhotoResponseDto> getAllPhotosForCar(Long carId) {
        carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Car not found with ID: " + carId));

        List<CarPhoto> carPhotos = carPhotoRepository.findByCarIdAndIsDeletedFalse(carId);

        return carPhotos.stream()
                .map(carPhotoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteCarPhotosFromCloud(List<CarPhoto> carPhotos) {
        for (CarPhoto carPhoto : carPhotos) {
            photoStorageService.deletePhotoByUrl(carPhoto.getPhotoUrl());
        }
    }

    @Override
    public void deleteCarPhotoFromCloud(CarPhoto carPhoto) {
//        if (carPhoto.isDeleted()) {
            photoStorageService.deletePhotoByUrl(carPhoto.getPhotoUrl());
//        }
    }
}
