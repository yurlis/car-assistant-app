package yurlis.carassistantapp.mapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;
import yurlis.carassistantapp.config.MapperConfig;
import yurlis.carassistantapp.dto.car.CarWithoutPhotosDto;
import yurlis.carassistantapp.dto.car.CreateCarWithoutPhotosDto;
import yurlis.carassistantapp.dto.car.UpdateCarRequestDto;
import yurlis.carassistantapp.dto.carphoto.CarPhotoDto;
import yurlis.carassistantapp.model.Car;
import yurlis.carassistantapp.model.FuelType;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(config = MapperConfig.class)
@Component
public interface CarMapper {
    @Mapping(target = "fuelTypesIds", ignore = true)
//    @Mapping(target = "carPhotos", ignore = true)
    @Mapping(source = "user.id", target = "userId")
    CarWithoutPhotosDto toDto(Car car);

    @AfterMapping
    default void setFuelTypeIds(@MappingTarget CarWithoutPhotosDto carWithoutPhotosDto, Car car) {
        Set<Long> fuelTypeIds = car.getFuelTypes().stream()
                .map(FuelType::getId)
                .collect(Collectors.toSet());
        carWithoutPhotosDto.setFuelTypesIds(fuelTypeIds);
    }

//    @AfterMapping
//    default void setCarPhotos(@MappingTarget CarWithoutPhotosDto carWithoutPhotosDto, Car car) {
//        Set<CarPhotoDto> carPhotos = car.getCarPhotos().stream()
//                .map(carPhoto -> {
//                    CarPhotoDto carPhotoDto = new CarPhotoDto();
//                    carPhotoDto.setId(carPhoto.getId());
//                    carPhotoDto.setPhotoUrl(carPhoto.getPhotoUrl());
//                    return carPhotoDto;
//                })
//                .collect(Collectors.toSet());
//        carWithoutPhotosDto.setCarPhotos(carPhotos);
//    }

    default void updateCarFromDto(Car car, UpdateCarRequestDto dto) {
        dto.getBrand().ifPresent(car::setBrand);
        dto.getModel().ifPresent(car::setModel);
        dto.getYearOfManufacture().ifPresent(car::setYearOfManufacture);
        dto.getVinCode().ifPresent(car::setVinCode);
        dto.getColorCode().ifPresent(car::setColorCode);
        dto.getMileage().ifPresent(car::setMileage);
//        dto.getFuelTypes().ifPresent(car::setFuelTypes);
//        dto.getPhotos().ifPresent(photos -> {
//       });
    }

    @Mapping(target = "carPhotos", ignore = true)
    @Mapping(target = "fuelTypes", ignore = true)
    @Mapping(source = "purchaseDate", target = "purchaseDate")
    Car toModel(CreateCarWithoutPhotosDto requestDto);

    @AfterMapping
    default void setFuelTypes(@MappingTarget Car car, CreateCarWithoutPhotosDto requestDto) {
        if (requestDto.getFuelTypes() != null) {
            Set<FuelType> fuelTypes = mapFuelTypeIdsToFuelTypes(requestDto.getFuelTypes());
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

    default LocalDate mapPurchaseDate(String purchaseDate) {
        if (purchaseDate == null || purchaseDate.isEmpty()) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return LocalDate.parse(purchaseDate, formatter);
    }
}
