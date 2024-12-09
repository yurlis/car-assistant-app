package yurlis.carassistantapp.validator.password;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordCustomValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface PasswordConstraint {
    String message() default "must be between 8 and 35 characters long "
            + "and include one number, one letter, and one special character";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
