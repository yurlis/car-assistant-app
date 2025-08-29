package yurlis.carassistantapp.validator.password;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class PasswordCustomValidator implements ConstraintValidator<PasswordConstraint, String> {

    private int min;
    private int max;
    private Pattern pattern;

    // Регулярний вираз перевіряє:
    // - принаймні одну маленьку літеру
    // - принаймні одну велику літеру
    // - принаймні одну цифру
    // - принаймні один спецсимвол із набору @$!%*#?&.
    private static final String DEFAULT_PASSWORD_REGEXP =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&.])[A-Za-z\\d@$!%*#?&.]+$";

    @Override
    public void initialize(PasswordConstraint constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
        this.pattern = Pattern.compile(DEFAULT_PASSWORD_REGEXP);
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null) {
            // Можна тут вирішити, чи null - валідний (залежить від логіки)
            return true;
        }

        if (password.length() < min || password.length() > max) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    String.format("Password length must be between %d and %d characters", min, max)
            ).addConstraintViolation();
            return false;
        }

        if (!pattern.matcher(password).matches()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Password must contain at least one uppercase letter, one lowercase letter, one digit and one special character (@$!%*#?&.)"
            ).addConstraintViolation();
            return false;
        }

        return true;
    }
}
