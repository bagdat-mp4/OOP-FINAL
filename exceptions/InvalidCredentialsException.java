package exceptions;

/**
 * Exception thrown when invalid login credentials are provided
 */
public class InvalidCredentialsException extends Exception {

    public InvalidCredentialsException(String message) {
        super(message);
    }

}