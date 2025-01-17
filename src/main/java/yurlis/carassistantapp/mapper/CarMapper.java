package yurlis.carassistantapp.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import yurlis.carassistantapp.config.MapperConfig;
import yurlis.carassistantapp.dto.car.CarWithoutPhotosResponseDto;
import yurlis.carassistantapp.dto.car.CreateCarWithoutPhotosRequestDto;
import yurlis.carassistantapp.dto.car.UpdateCarWithoutPhotosRequestDto;
import yurlis.carassistantapp.model.Car;
import yurlis.carassistantapp.model.FuelType;

import java.sql.Timestamp;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class)
@Component
public interface CarMapper {
    @Mapping(target = "fuelTypesIds", ignore = true) // Автоматично встановлюється в @AfterMapping
    @Mapping(source = "user.id", target = "userId")
    CarWithoutPhotosResponseDto toDto(Car car);

    //@Mapping(target = "carPhotos", ignore = true)
    @Mapping(target = "fuelTypes", ignore = true)
    Car toModel(CreateCarWithoutPhotosRequestDto requestDto);

    @AfterMapping
    default void setFuelTypeIds(@MappingTarget CarWithoutPhotosResponseDto carWithoutPhotosResponseDto, Car car) {
        Set<Long> fuelTypeIds = car.getFuelTypes().stream()
                .map(FuelType::getId)
                .collect(Collectors.toSet());
        carWithoutPhotosResponseDto.setFuelTypesIds(fuelTypeIds);
    }

    @AfterMapping
    default void setFuelTypes(@MappingTarget Car car, CreateCarWithoutPhotosRequestDto requestDto) {
        if (requestDto.getFuelTypes() != null) {
            Set<FuelType> fuelTypes = mapFuelTypeIdsToFuelTypes(requestDto.getFuelTypes());
            car.setFuelTypes(fuelTypes);
        }
    }

    default void updateCarFromDto(@MappingTarget Car car, UpdateCarWithoutPhotosRequestDto dto) {
        if (dto.getBrand() != null && !dto.getBrand().isEmpty()) {
            car.setBrand(dto.getBrand());
        }
        if (dto.getModel() != null && !dto.getModel().isEmpty()) {
            car.setModel(dto.getModel());
        }
        if (dto.getYearOfManufacture() != null) {
            car.setYearOfManufacture(dto.getYearOfManufacture());
        }
        if (dto.getVinCode() != null && !dto.getVinCode().isEmpty()) {
            car.setVinCode(dto.getVinCode());
        }
        if (dto.getColorCode() != null && !dto.getColorCode().isEmpty()) {
            car.setColorCode(dto.getColorCode());
        }
        if (dto.getMileage() != null) {
            car.setMileage(dto.getMileage());
        }
        if (dto.getFuelTypes() != null) {
            Set<FuelType> fuelTypes = mapFuelTypeIdsToFuelTypes(dto.getFuelTypes());
            car.setFuelTypes(fuelTypes);
        }
    }

    default Set<FuelType> mapFuelTypeIdsToFuelTypes(Set<Long> fuelTypeIds) {
        return fuelTypeIds.stream()
                .map(fuelTypeId -> {
                    FuelType fuelType = new FuelType();
                    fuelType.setId(fuelTypeId);
                    return fuelType;
                })
                .collect(Collectors.toSet());
    }

    default Long mapTimestampToLong(Timestamp timestamp) {
        return timestamp != null ? timestamp.getTime() : null;
    }
}
