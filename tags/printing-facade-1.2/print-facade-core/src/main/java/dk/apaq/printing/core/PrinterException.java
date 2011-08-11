package dk.apaq.printing.core;

/**
 *
 * @author michaelzachariassenkrog
 */
public class PrinterException extends RuntimeException {

    public PrinterException(String message) {
        super(message);
    }

    public PrinterException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrinterException(Throwable cause) {
        super(cause);
    }
    
}
