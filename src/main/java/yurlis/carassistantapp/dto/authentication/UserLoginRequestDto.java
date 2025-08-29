package yurlis.carassistantapp.dto.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.Length;
import yurlis.carassistantapp.validator.password.PasswordConstraint;

public record UserLoginRequestDto(
        @NotEmpty(message = "Email cannot be empty")
        @Size(min = 8, max = 50, message = "Email must be between 8 and 50 characters")
        @Email(message = "Must be a valid email address")
        String email,
        @NotEmpty(message = "Password cannot be empty")
        @PasswordConstraint(min = 8, max = 35)
        String password
) {
}
