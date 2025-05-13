package banking.p2p_transfer.exception;

public class DatabaseException extends RuntimeException {
    public DatabaseException(String message, Throwable cause) {
        super(message, cause);
    }
}