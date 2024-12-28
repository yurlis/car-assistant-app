package yurlis.carassistantapp.validator.date;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateFormatValidator implements ConstraintValidator<ValidDateFormat, String> {

    private String pattern;

    @Override
    public void initialize(ValidDateFormat constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String date, ConstraintValidatorContext context) {
        if (date == null || date.isEmpty()) {
            return true;
        }

        // Перевірка на правильний формат дати
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate parsedDate = LocalDate.parse(date, formatter);

            // Перевірка, чи дата не в майбутньому
            if (parsedDate.isAfter(LocalDate.now())) {
                context.disableDefaultConstraintViolation();  // Вимикає стандартне повідомлення про помилку
                context.buildConstraintViolationWithTemplate("Purchase date cannot be in the future")  // Створює нове повідомлення
                        .addConstraintViolation();  // Додає його до контексту валідації
                return false;  // Валідація не пройшла
            }
            return true;  // Валідація пройшла успішно
        } catch (DateTimeParseException e) {
            return false;  // Якщо не вдалося розпарсити дату, валідація не пройшла
        }
    }
}
