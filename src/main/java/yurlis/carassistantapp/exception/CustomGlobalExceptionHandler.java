package yurlis.carassistantapp.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());

        Map<String, List<String>> errors = new LinkedHashMap<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String field = "unknownField";
            String message = error.getDefaultMessage(); // Отримуємо повідомлення з defaultMessage

            // Якщо це FieldError, отримуємо ім'я поля
            if (error instanceof FieldError) {
                field = ((FieldError) error).getField();
            }
            // Якщо це ViolationObjectError (JSR-303)
            else {
                // Перевіряємо, чи є перший код в масиві рівним "FieldMatch.userRegistrationRequestDto"
                String errorCode = error.getCodes()[0];

                if ("FieldMatch.userRegistrationRequestDto".equals(errorCode)) {
                    field = "password";
                }
            }

            errors.computeIfAbsent(field, k -> new ArrayList<>()).add(message);
        });

        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Object> handleRegistrationException(RegistrationException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());

        Map<String, List<String>> errors = new LinkedHashMap<>();

        List<String> emailErrors = new ArrayList<>();
        emailErrors.add(ex.getMessage());

        List<String> passwordErrors = new ArrayList<>();
        passwordErrors.add(ex.getMessage());

        errors.put("email", emailErrors);
        errors.put("password", passwordErrors);

        body.put("errors", errors);
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            return ((FieldError) e).getDefaultMessage();
        }

        return e.getDefaultMessage();
    }
}
