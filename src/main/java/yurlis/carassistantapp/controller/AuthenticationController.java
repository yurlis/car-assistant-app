package yurlis.carassistantapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import yurlis.carassistantapp.dto.authentication.UserLoginRequestDto;
import yurlis.carassistantapp.dto.authentication.UserLoginResponseDto;
import yurlis.carassistantapp.dto.authentication.UserRegistrationRequestDto;
import yurlis.carassistantapp.dto.authentication.UserRegistrationResponseDto;
import yurlis.carassistantapp.exception.RegistrationException;
import yurlis.carassistantapp.security.AuthenticationService;
import yurlis.carassistantapp.service.user.UserService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Tag(
        name = "Authentication Management",
        description = "Endpoints for handling the authentication processes of the online store."
)
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;

    @PostMapping("/registration")
    @Operation(summary = "Register a new user",
            description = "Performs a registration of a new user and add a new user to the DB")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRegistrationResponseDto register(@RequestBody @Valid UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        return userService.register(requestDto);
    }

    @PostMapping("/login")
    @Operation(
            summary = "Authenticate a user",
            description = "Performs authentication by validating user credentials and returning an access token."
    )
    @ResponseStatus(HttpStatus.OK)
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.authenticate(requestDto);
    }
}
