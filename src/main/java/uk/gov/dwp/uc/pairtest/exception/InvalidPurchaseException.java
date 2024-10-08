package uk.gov.dwp.uc.pairtest.exception;

public class InvalidPurchaseException extends RuntimeException  {

    private int statusCode = 0;

    public int getStatusCode() {
        return statusCode;
    }

    public InvalidPurchaseException(String message) {
        super(message);
    }

    public InvalidPurchaseException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public InvalidPurchaseException(String message, Throwable cause) {
        super(message, cause);
    }
}
