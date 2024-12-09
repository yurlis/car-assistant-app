package yurlis.carassistantapp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import yurlis.carassistantapp.validator.password.FieldMatch;
import yurlis.carassistantapp.validator.password.PasswordConstraint;

@Data
@FieldMatch.List({
        @FieldMatch(first = "password", second = "repeatPassword",
                message = "Passwords must match")
})
public class UserRegistrationRequestDto {
    @NotBlank(message = "Email cannot be empty")
    @Email(message = "Must be a valid email address")
    private String email;
    @NotBlank(message = "Password cannot be empty")
    @PasswordConstraint
    private String password;
    @NotBlank(message = "Repeat password cannot be empty")
    @Length(min = 8, max = 35, message = "Password must be between 8 and 35 characters")
    private String repeatPassword;
//    @NotBlank(message = "First name cannot be empty")
    private String firstName;
//    @NotBlank(message = "Last name cannot be empty")
    private String lastName;
}
