package data_access;

/**
 * Runtime failure raised by the outer Cito API adapter.
 */
public class CitoApiException extends RuntimeException {
    public CitoApiException(String message) {
        super(message);
    }

    public CitoApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
