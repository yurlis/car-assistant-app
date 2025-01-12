package yurlis.carassistantapp.exception;

public class DuplicateVinCodeException extends RuntimeException {
    public DuplicateVinCodeException(String message) {
        super(message);
    }
}
