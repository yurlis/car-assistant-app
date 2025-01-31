package yurlis.carassistantapp.service.car;

import org.springframework.web.multipart.MultipartFile;
import yurlis.carassistantapp.dto.car.CarPhotoResponseDto;
import yurlis.carassistantapp.dto.car.CarWithoutPhotosResponseDto;
import yurlis.carassistantapp.dto.car.CreateCarWithoutPhotosRequestDto;
import yurlis.carassistantapp.dto.car.UpdateCarWithoutPhotosRequestDto;
import yurlis.carassistantapp.model.Car;
import yurlis.carassistantapp.model.CarPhoto;

import java.util.List;

public interface CarService {
    CarWithoutPhotosResponseDto save(Long userId, CreateCarWithoutPhotosRequestDto carDto);
    List<CarWithoutPhotosResponseDto> findAllByUserId(Long userId);
    CarWithoutPhotosResponseDto getById(Long id);
    CarWithoutPhotosResponseDto update(Long id, UpdateCarWithoutPhotosRequestDto updateCarWithoutPhotosRequestDto);
    void deleteById(Long id, Long userId);
    void deleteCarTransactional(Long id);
    CarPhotoResponseDto uploadPhoto(Long carId, MultipartFile photo);
    void deletePhotoById(Long photoId);
    void deletePhotoTransactional(Long photoId);
}
