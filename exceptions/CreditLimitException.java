package exceptions;

/**
 * Exception thrown when student exceeds credit limit
 */
public class CreditLimitException extends Exception {

    public CreditLimitException(String message) {
        super(message);
    }

}