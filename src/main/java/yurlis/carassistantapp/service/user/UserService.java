package yurlis.carassistantapp.service.user;

import yurlis.carassistantapp.dto.authentication.UserRegistrationRequestDto;
import yurlis.carassistantapp.dto.authentication.UserRegistrationResponseDto;
import yurlis.carassistantapp.exception.RegistrationException;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;
}
