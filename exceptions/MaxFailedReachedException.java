package exceptions;

/**
 * Exception thrown when student reaches maximum failed courses
 */
public class MaxFailedReachedException extends Exception {

    public MaxFailedReachedException(String message) {
        super(message);
    }

}