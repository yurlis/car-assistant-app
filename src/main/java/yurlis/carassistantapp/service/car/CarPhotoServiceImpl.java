package yurlis.carassistantapp.service.car;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yurlis.carassistantapp.dto.car.CarPhotoResponseDto;
import yurlis.carassistantapp.mapper.CarPhotoMapper;
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
    public List<CarPhotoResponseDto> getAllPhotosForCar(Long carId, Long userId) {
        carRepository.findByIdAndUserId(carId, userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Car with ID %d not found for user with ID %d.", carId, userId)
                ));

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
