package yurlis.carassistantapp.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import yurlis.carassistantapp.config.MapperConfig;
import yurlis.carassistantapp.dto.carrefuel.CarRefuelResponseDto;
import yurlis.carassistantapp.dto.carrefuel.CreateCarRefuelRequestDto;
import yurlis.carassistantapp.model.CarRefuel;
import yurlis.carassistantapp.model.FuelType;

@Mapper(config = MapperConfig.class)
@Component
public interface CarRefuelMapper extends CommonMapper, CommonTimeMapper {
    @Mapping(source = "car.id", target = "carId")
    @Mapping(target = "formattedRefuelTime", expression = "java(formatPurchaseDate(carRefuel.getRefuelTime()))")
    CarRefuelResponseDto toDto(CarRefuel carRefuel);

    @Mapping(target = "fuelType", ignore = true)
    CarRefuel toModel(CreateCarRefuelRequestDto requestDto);

    @AfterMapping
    default void setFuelType(@MappingTarget CarRefuel carRefuel, CreateCarRefuelRequestDto requestDto) {
        if (requestDto.getFuelType() != null) {
            FuelType fuelType = mapFuelTypeIdToFuelType(requestDto.getFuelType());
            carRefuel.setFuelType(fuelType);
        }
    }

    default FuelType mapFuelTypeIdToFuelType(Long fuelTypeId) {
        FuelType fuelType = new FuelType();
        fuelType.setId(fuelTypeId);
        return fuelType;
    }

    default void updateFromDto(@MappingTarget CarRefuel refuel, CreateCarRefuelRequestDto dto) {
        if (dto.getRefuelTime() != null) {
            refuel.setRefuelTime(dto.getRefuelTime());
        }
        if (dto.getGasStationName() != null && !dto.getGasStationName().isEmpty()) {
            refuel.setGasStationName(dto.getGasStationName());
        }
        if (dto.getFuelQuantity() != null) {
            refuel.setFuelQuantity(dto.getFuelQuantity());
        }
        if (dto.getCostPerLiter() != null) {
            refuel.setCostPerLiter(dto.getCostPerLiter());
        }
        if (dto.getMovementType() != null) {
            refuel.setMovementType(CarRefuel.MovementType.valueOf(dto.getMovementType()));
        }
        if (dto.getFuelType() != null) {
            FuelType fuelType = mapFuelTypeIdToFuelType(dto.getFuelType());
            refuel.setFuelType(fuelType);
        }
    }
}
