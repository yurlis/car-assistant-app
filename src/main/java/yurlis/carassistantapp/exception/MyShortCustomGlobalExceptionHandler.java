package yurlis.carassistantapp.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

//@ControllerAdvice
public class MyShortCustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

//    ------ це видає все, крім помилок MatchFields
//{
//    "timestamp": "2024-12-17T14:48:59.6820911",
//        "status": "BAD_REQUEST",
//        "errors": {
//              "firstName": "First name cannot be empty",
//              "repeatPassword": "Password must be between 8 and 35 characters",
//              "email": "Must be a valid email address"
//      }
//}
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("HTTP_status", createHttpStatus(HttpStatus.BAD_REQUEST));

        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
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

    private Map<String, Object> createHttpStatus(HttpStatus status) {
        Map<String, Object> httpStatus = new LinkedHashMap<>();
        httpStatus.put("code", status.value());
        httpStatus.put("message", status.toString());
        return httpStatus;
    }
}
