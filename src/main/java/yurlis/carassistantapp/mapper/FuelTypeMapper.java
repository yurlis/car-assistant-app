package yurlis.carassistantapp.mapper;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;
import yurlis.carassistantapp.config.MapperConfig;
import yurlis.carassistantapp.dto.fueltypes.FuelTypeResponseDto;
import yurlis.carassistantapp.model.FuelType;

@Mapper(config = MapperConfig.class)
@Component
public interface FuelTypeMapper {
    FuelTypeResponseDto toDto(FuelType fuelType);
}
