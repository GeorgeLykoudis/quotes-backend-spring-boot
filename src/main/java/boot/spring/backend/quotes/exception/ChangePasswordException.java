package boot.spring.backend.quotes.exception;

public class ChangePasswordException extends RuntimeException {

    public ChangePasswordException() {
        super();
    }

    public ChangePasswordException(String message) {
        super(message);
    }

}
