package yurlis.carassistantapp.validator.yearofmanufacture;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class YearOfManufactureValidator implements ConstraintValidator<ValidYearOfManufacture, Integer> {

    @Override
    public void initialize(ValidYearOfManufacture constraintAnnotation) {
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        int currentYear = LocalDate.now().getYear();
        return value <= currentYear;
    }
}
