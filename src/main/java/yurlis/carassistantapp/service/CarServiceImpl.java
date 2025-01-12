package yurlis.carassistantapp.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yurlis.carassistantapp.dto.car.CarWithoutPhotosDto;
import yurlis.carassistantapp.dto.car.CreateCarWithoutPhotosRequestDto;
import yurlis.carassistantapp.dto.car.UpdateCarWithoutPhotosRequestDto;
import yurlis.carassistantapp.exception.DuplicateVinCodeException;
import yurlis.carassistantapp.mapper.CarMapper;
import yurlis.carassistantapp.model.Car;
import yurlis.carassistantapp.repository.car.CarRepository;
import yurlis.carassistantapp.repository.fueltype.FuelTypeRepository;
import yurlis.carassistantapp.repository.user.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final FuelTypeRepository fuelTypeRepository;
    private final CarMapper carMapper;

    @Override
    public CarWithoutPhotosDto save(Long userId, CreateCarWithoutPhotosRequestDto requestDto) {
        Car car = carMapper.toModel(requestDto);
        car.setUser(userRepository.getReferenceById(userId));
        if (carRepository.existsByVinCode(car.getVinCode())) {
            throw new DuplicateVinCodeException("Car with this VIN code already exists.");
        }

        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    public List<CarWithoutPhotosDto> findAllByUserId(Long userId) {
        return carRepository.findByUserId(userId).stream()
                .map(carMapper::toDto)
                .toList();
    }

    @Override
    public CarWithoutPhotosDto getById(Long id) {
        Car car = carRepository
                .findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Car not found with ID: " + id)
                );
        return carMapper.toDto(car);
    }

    @Transactional
    @Override
    public CarWithoutPhotosDto update(Long id, UpdateCarWithoutPhotosRequestDto updateRequestDto) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Car with ID " + id + " not found"));

        carMapper.updateCarFromDto(car, updateRequestDto);

        Car updatedCar = carRepository.save(car);

        return carMapper.toDto(updatedCar);
    }

    @Override
    public void deleteById(Long id) {
        if (!carRepository.existsById(id)) {
            throw new EntityNotFoundException("Car with ID " + id + " not found.");
        }
        carRepository.deleteById(id);
    }
}
