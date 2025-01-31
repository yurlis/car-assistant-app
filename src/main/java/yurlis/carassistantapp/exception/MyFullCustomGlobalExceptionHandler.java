package yurlis.carassistantapp.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class MyFullCustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final int FIRST_FIELD_INDEX = 1;
    private static final int SECOND_FIELD_INDEX = 2;

//     повна реалізація в форматі
//        {
//            "timestamp":"2024-12-17T14:52:13.6498567",
//            "status":"BAD_REQUEST",
//            "errors":{
//                "firstName":"First name cannot be empty",
//                "repeatPassword":"Passwords must match",
//                "email":"Must be a valid email address",
//                "password":"Passwords must match"
//            }
//        }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("HTTP_status", createHttpStatus(HttpStatus.BAD_REQUEST));

        // Отримуємо помилки полів
        Map<String, String> fieldErrors = getFieldErrors(ex);

        // Отримуємо загальні помилки
        Map<String, String> globalErrors = getGlobalErrors(ex);

        // Об'єднуємо всі помилки
        Map<String, String> allErrors = new LinkedHashMap<>();
        allErrors.putAll(fieldErrors);
        allErrors.putAll(globalErrors);

        body.put("errors", allErrors); // Додаємо до відповіді
        return new ResponseEntity<>(body, headers, status);
    }

    // Метод для обробки помилок полів
    private Map<String, String> getFieldErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage,
                        (existing, replacement) -> existing // Уникаємо дублікатів
                ));
    }

    // Метод для обробки загальних помилок
    private Map<String, String> getGlobalErrors(MethodArgumentNotValidException ex) {
        return ex.getBindingResult().getGlobalErrors().stream()
                .map(error -> {
                    String objectName = error.getObjectName();
                    String errorMessage = error.getDefaultMessage();

                    // Перевірка на помилки з анотацією FieldMatch
                    if (error.getCodes() != null && Arrays.asList(error.getCodes()).contains("FieldMatch")) {
                        // Отримуємо поля, які вказані в анотації FieldMatch
                        Object[] arguments = error.getArguments();
                        if (arguments != null && arguments.length > 1) {
                            String firstField = arguments[FIRST_FIELD_INDEX].toString();
                            String secondField = arguments[SECOND_FIELD_INDEX].toString();

                            return Arrays.asList(
                                    new AbstractMap.SimpleEntry<>(firstField, errorMessage),
                                    new AbstractMap.SimpleEntry<>(secondField, errorMessage)
                            );
                        }
                    }
                    return List.of(new AbstractMap.SimpleEntry<>(objectName, errorMessage));
                })
                .flatMap(List::stream)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing // Уникаємо дублікатів
                ));
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Object> handleRegistrationException(RegistrationException ex) {
        Map<String, Object> body = new LinkedHashMap<>();
        String errorMessage = ex.getMessage();

        body.put("timestamp", LocalDateTime.now());
        body.put("HTTP_status", createHttpStatus(HttpStatus.CONFLICT));
        body.put("errors", errorMessage);

        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateVinCodeException.class)
    public ResponseEntity<Object> handleDuplicateVinCodeException(DuplicateVinCodeException ex) {
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", LocalDateTime.now());
        body.put("HTTP_status", createHttpStatus(HttpStatus.BAD_REQUEST));
        body.put("errors", ex.getMessage());

        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    private Map<String, Object> createHttpStatus(HttpStatus status) {
        Map<String, Object> httpStatus = new LinkedHashMap<>();
        httpStatus.put("code", status.value());
        httpStatus.put("message", status.toString());
        return httpStatus;
    }
}
