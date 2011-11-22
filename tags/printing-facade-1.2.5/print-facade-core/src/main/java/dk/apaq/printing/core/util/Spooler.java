package dk.apaq.printing.core.util;

import dk.apaq.printing.core.PrinterJob;
import java.io.OutputStream;

/**
 *
 * @author krog
 */
public interface Spooler {
    OutputStream getOutputStreamForJob(PrinterJob job);
}
