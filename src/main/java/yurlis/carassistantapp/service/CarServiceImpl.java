package yurlis.carassistantapp.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import yurlis.carassistantapp.dto.car.CarWithoutPhotosDto;
import yurlis.carassistantapp.dto.car.CreateCarWithoutPhotosDto;
import yurlis.carassistantapp.dto.car.UpdateCarRequestDto;
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
    public CarWithoutPhotosDto save(Long userId, CreateCarWithoutPhotosDto requestDto) {
        Car car = carMapper.toModel(requestDto);
        car.setUser(userRepository.getReferenceById(userId));
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

    @Override
    public CarWithoutPhotosDto update(Long id, UpdateCarRequestDto updateCarRequestDto) {
        return null;
    }

    @Override
    public void deleteById(Long id) {
        if (!carRepository.existsById(id)) {
            throw new EntityNotFoundException("Car with ID " + id + " not found.");
        }
        carRepository.deleteById(id);
    }
}
