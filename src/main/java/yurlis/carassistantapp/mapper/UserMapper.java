package yurlis.carassistantapp.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.stereotype.Component;
import yurlis.carassistantapp.config.MapperConfig;
import yurlis.carassistantapp.dto.UserRegistrationRequestDto;
import yurlis.carassistantapp.dto.UserRegistrationResponseDto;
import yurlis.carassistantapp.model.User;

@Mapper(config = MapperConfig.class, uses = PasswordEncoderHelper.class)
@Component
public interface UserMapper {
    UserRegistrationResponseDto toRegistrationResponseDto(User user);

    @Mappings({
            @Mapping(target = "password", source = "password", qualifiedByName = "encodedPassword")
    })
    User toUserModel(UserRegistrationRequestDto userRegistrationRequestDto);
}
