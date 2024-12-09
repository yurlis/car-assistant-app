package yurlis.carassistantapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

public record UserLoginRequestDto(
        @NotEmpty(message = "cannot be empty")
        @Length(min = 8, max = 35, message = "password must be between 8 and 35 characters")
        @Email
        String email,
        @NotEmpty(message = "cannot be empty")
        @Length(min = 8, max = 35, message = "password must be between 8 and 35 characters")
        String password
) {
}
