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
import java.util.stream.Collectors;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    @Override
//    protected ResponseEntity<Object> handleMethodArgumentNotValid(
//            MethodArgumentNotValidException ex,
//            HttpHeaders headers,
//            HttpStatusCode status,
//            WebRequest request) {
//        Map<String, Object> body = new LinkedHashMap<>();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status", HttpStatus.BAD_REQUEST);
//
//        List<String> errors = ex.getBindingResult().getAllErrors().stream()
//                .map(e -> getErrorMessage(e))
//                .toList();
//        body.put("errors", errors);
//        return new ResponseEntity<>(body, headers, status);
//    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST);

        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));

        body.put("errors", errors); // Додаємо до відповіді
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

//    @ExceptionHandler(RegistrationException.class)
//    public ResponseEntity<Object> handleRegistrationException(RegistrationException ex) {
//        Map<String, Object> body = new LinkedHashMap<>();
//        String errorMessage = ex.getMessage();
//        body.put("timestamp", LocalDateTime.now());
//        body.put("status", HttpStatus.BAD_REQUEST);
//        body.put("errors", errorMessage);
//        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
//    }

    private String getErrorMessage(ObjectError e) {
        if (e instanceof FieldError) {
            return ((FieldError) e).getDefaultMessage();
        }

        return e.getDefaultMessage();
    }
}
