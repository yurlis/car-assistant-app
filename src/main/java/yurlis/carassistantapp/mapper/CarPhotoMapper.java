package yurlis.carassistantapp.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import yurlis.carassistantapp.config.MapperConfig;
import yurlis.carassistantapp.dto.carphoto.CarPhotoDto;
import yurlis.carassistantapp.model.CarPhoto;

@Mapper(config = MapperConfig.class)
@Component
public interface CarPhotoMapper {
    CarPhotoDto toDto(CarPhoto carPhoto);
    CarPhoto toModel(CarPhotoDto carPhotoDto);
}
