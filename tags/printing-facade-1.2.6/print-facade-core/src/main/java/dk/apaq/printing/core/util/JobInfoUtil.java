package dk.apaq.printing.core.util;

import dk.apaq.printing.core.PrinterJob;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;

/**
 *
 * @author michael
 */
public class JobInfoUtil {
    public static void writeJobInfo(PrinterJob printerJob, OutputStream os) throws IOException {

        Writer w = new OutputStreamWriter(os);
        Properties p = new Properties();
        p.setProperty("printerId", printerJob.getPrinter().getId());
        p.setProperty("paper", printerJob.getPaper().toString());
        p.setProperty("margin", printerJob.getMargin().toString());
        p.setProperty("copies", Integer.toString(printerJob.getCopies()));
        p.store(w, null);

    }
}
