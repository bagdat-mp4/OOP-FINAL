package exceptions;

/**
 * Exception thrown when supervisor's H-index is too low
 */
public class LowHIndexException extends Exception {

    public LowHIndexException(String message) {
        super(message);
    }

}