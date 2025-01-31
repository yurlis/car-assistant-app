package yurlis.carassistantapp.service.carrefuel;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yurlis.carassistantapp.dto.carrefuel.CarRefuelResponseDto;
import yurlis.carassistantapp.dto.carrefuel.CreateCarRefuelRequestDto;
import yurlis.carassistantapp.dto.pagination.CustomPage;
import yurlis.carassistantapp.mapper.CarRefuelMapper;
import yurlis.carassistantapp.model.Car;
import yurlis.carassistantapp.model.CarRefuel;
import yurlis.carassistantapp.repository.car.CarRepository;
import yurlis.carassistantapp.repository.fueltype.FuelTypeRepository;
import yurlis.carassistantapp.repository.carrefuel.CarRefuelRepository;

import java.sql.Timestamp;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CarRefuelServiceImpl implements CarRefuelService {
    private final CarRefuelRepository carRefuelRepository;
    private final FuelTypeRepository fuelTypeRepository;
    private final CarRepository carRepository;
    private final CarRefuelMapper carRefuelMapper;

    @Override
    @Transactional
    public CarRefuelResponseDto createRefuel(Long carId, CreateCarRefuelRequestDto requestDto) {
        Car car = getCarAndCheckFuelType(carId, requestDto);

        CarRefuel carRefuel = carRefuelMapper.toModel(requestDto);
        carRefuel.setCar(car);
        carRefuelRepository.save(carRefuel);
        return carRefuelMapper.toDto(carRefuel);
    }

    @Override
    public CarRefuelResponseDto updateRefuel(Long refuelId, CreateCarRefuelRequestDto refuelRequestDto) {
        CarRefuel carRefuel = getCarRefuel(refuelId);

        carRefuelMapper.updateFromDto(carRefuel, refuelRequestDto);

        CarRefuel updatedCarRefuel = carRefuelRepository.save(carRefuel);
        return carRefuelMapper.toDto(updatedCarRefuel);
    }

    @Override
    @Transactional
    public void deleteCarRefuel(Long refuelId) {
        carRefuelRepository.delete(getCarRefuel(refuelId));
    }

    @Override
    public CustomPage<CarRefuelResponseDto> getRefuelsForCar(
            Long carId, Timestamp startDate, Timestamp endDate, Long fuelType, Pageable pageable
    ) {
        Page<CarRefuel> refuels;

        if (fuelType == null && startDate == null && endDate == null) {
            refuels = carRefuelRepository.findAllByCarId(carId, pageable);
        } else if (fuelType == null) {
            refuels = carRefuelRepository.findAllByCarIdAndRefuelTimeBetween(carId, startDate, endDate, pageable);
        } else if (startDate == null && endDate == null) {
            refuels = carRefuelRepository.findAllByCarIdAndFuelTypeId(carId, fuelType, pageable);
        } else {
            refuels = carRefuelRepository.findAllByCarIdAndFuelTypeIdAndRefuelTimeBetween(carId, fuelType, startDate, endDate, pageable);
        }

        return CustomPage.from(refuels.map(carRefuelMapper::toDto));
    }

    private CarRefuel getCarRefuel(Long refuelId) {
        CarRefuel carRefuel = carRefuelRepository
                .findById(refuelId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Car Refuel with id " + refuelId + " not found")
                );
        return carRefuel;
    }
    
    private Car getCarAndCheckFuelType(Long carId, CreateCarRefuelRequestDto requestDto) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("Car with ID " + carId + " not found."));

        boolean fuelTypeSupported = car.getFuelTypes().stream()
                .anyMatch(fuel -> fuel.getId().equals(requestDto.getFuelType()));

        if (!fuelTypeSupported) {
            throw new IllegalArgumentException("Fuel type with ID " + requestDto.getFuelType() + " is not registered for car ID " + carId);
        }
        return car;
    }
}