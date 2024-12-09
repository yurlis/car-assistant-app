package yurlis.carassistantapp.service;

import yurlis.carassistantapp.dto.UserRegistrationRequestDto;
import yurlis.carassistantapp.dto.UserRegistrationResponseDto;
import yurlis.carassistantapp.exception.RegistrationException;

public interface UserService {
    UserRegistrationResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException;
}
