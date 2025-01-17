package yurlis.carassistantapp.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import yurlis.carassistantapp.dto.car.CarPhotoResponseDto;
import yurlis.carassistantapp.dto.car.CarWithoutPhotosResponseDto;
import yurlis.carassistantapp.dto.car.CreateCarWithoutPhotosRequestDto;
import yurlis.carassistantapp.dto.car.UpdateCarWithoutPhotosRequestDto;
import yurlis.carassistantapp.exception.DuplicateVinCodeException;
import yurlis.carassistantapp.mapper.CarMapper;
import yurlis.carassistantapp.mapper.CarPhotoMapper;
import yurlis.carassistantapp.model.Car;
import yurlis.carassistantapp.model.CarPhoto;
import yurlis.carassistantapp.repository.car.CarRepository;
import yurlis.carassistantapp.repository.carphoto.CarPhotoRepository;
import yurlis.carassistantapp.repository.user.UserRepository;
import yurlis.carassistantapp.service.photoservice.PhotoStorageService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final CarMapper carMapper;
    private final PhotoStorageService photoStorageService;
    private final CarPhotoRepository carPhotoRepository;
    private final CarPhotoMapper carPhotoMapper;
    private final CarPhotoService carPhotoService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CarWithoutPhotosResponseDto save(Long userId, CreateCarWithoutPhotosRequestDto requestDto) {
        Car car = carMapper.toModel(requestDto);
        car.setUser(userRepository.getReferenceById(userId));
        if (carRepository.existsByVinCode(car.getVinCode())) {
            throw new DuplicateVinCodeException("Car with this VIN code already exists.");
        }

        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    public List<CarWithoutPhotosResponseDto> findAllByUserId(Long userId) {
        return carRepository.findByUserId(userId).stream()
                .map(carMapper::toDto)
                .toList();
    }

    @Override
    public CarWithoutPhotosResponseDto getById(Long id) {
        Car car = carRepository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Car not found with ID: " + id)
                );
        return carMapper.toDto(car);
    }

    @Transactional
    @Override
    public CarWithoutPhotosResponseDto update(Long id, UpdateCarWithoutPhotosRequestDto updateRequestDto) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car with ID " + id + " not found"));

        carMapper.updateCarFromDto(car, updateRequestDto);

        Car updatedCar = carRepository.save(car);

        return carMapper.toDto(updatedCar);
    }

    @Override
    public void deleteById(Long id) {
        carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car with ID " + id + " not found."));


        List<CarPhoto> carPhotos = carPhotoRepository.findByCarIdAndIsDeletedFalse(id);
        deleteCarTransactional(id);
        carPhotoService.deleteCarPhotosFromCloud(carPhotos);
    }

    @Override
    @Transactional
    public void deleteCarTransactional(Long id) {
        carRepository.deleteById(id);
    }

    @Override
    @Transactional
    public CarPhotoResponseDto uploadPhoto(Long carId, MultipartFile photo) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Car not found with ID: " + carId));

        String photoUrl = photoStorageService.uploadPhoto(photo);

        CarPhoto carPhotoEntity = new CarPhoto();
        carPhotoEntity.setPhotoUrl(photoUrl);
        carPhotoEntity.setCar(car);

        carPhotoRepository.save(carPhotoEntity);

        return carPhotoMapper.toDto(carPhotoEntity);
    }

    @Override
    public void deletePhotoById(Long photoId) {
        CarPhoto carPhoto = carPhotoRepository.findById(photoId)
                .orElseThrow(() -> new EntityNotFoundException("Photo not found with ID: " + photoId));
        deletePhotoTransactional(photoId);
        carPhotoService.deleteCarPhotoFromCloud(carPhoto);
    }

    @Override
    @Transactional
    public void deletePhotoTransactional(Long photoId) {
        carPhotoRepository.deleteById(photoId);
    }
}


    /*
    @SpringBootTest
public class CarPhotoServiceTest {

    @Autowired
    private CarPhotoService carPhotoService;

    @MockBean
    private CarPhotoRepository carPhotoRepository;

    @MockBean
    private PhotoStorageService photoStorageService;

    @Test
    void deletePhotoById_ShouldDeletePhotoSuccessfully() {
        Long photoId = 1L;
        CarPhoto carPhoto = new CarPhoto();
        carPhoto.setId(photoId);
        carPhoto.setPhotoUrl("http://res.cloudinary.com/example/photo.jpg");

        Mockito.when(carPhotoRepository.findById(photoId)).thenReturn(Optional.of(carPhoto));

        carPhotoService.deletePhotoById(photoId);

        Mockito.verify(photoStorageService).deletePhotoByUrl(carPhoto.getPhotoUrl());
        Mockito.verify(carPhotoRepository).deleteById(photoId);
    }
}
     */
