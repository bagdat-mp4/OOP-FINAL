package exceptions;

/**
 * Exception thrown when user is not a researcher
 */
public class NotAResearcherException extends Exception {

    public NotAResearcherException(String message) {
        super(message);
    }

}