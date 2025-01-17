package yurlis.carassistantapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import yurlis.carassistantapp.config.MapperConfig;
import yurlis.carassistantapp.dto.car.CarPhotoResponseDto;
import yurlis.carassistantapp.dto.carphoto.CarPhotoDto;
import yurlis.carassistantapp.model.CarPhoto;

@Mapper(config = MapperConfig.class)
@Component
public interface CarPhotoMapper {
    @Mapping(target = "carId", source = "car.id")
    @Mapping(target = "photoId", source = "id")
    CarPhotoResponseDto toDto(CarPhoto carPhoto);
    CarPhoto toModel(CarPhotoResponseDto carPhotoDto);
}
