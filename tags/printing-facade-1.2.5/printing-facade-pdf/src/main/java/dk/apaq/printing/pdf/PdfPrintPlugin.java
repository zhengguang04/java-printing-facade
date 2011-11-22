package dk.apaq.printing.pdf;

import com.itextpdf.text.DocumentException;
import dk.apaq.printing.core.AbstractPrinterManagerPlugin;
import dk.apaq.printing.core.Margin;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterException;
import dk.apaq.printing.core.PrinterJob;
import dk.apaq.printing.core.PrinterState;
import dk.apaq.printing.core.util.PdfUtil;
import dk.apaq.printing.core.util.Spooler;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author krog
 */
public class PdfPrintPlugin extends AbstractPrinterManagerPlugin {

    private static final Logger LOG = LoggerFactory.getLogger(PdfPrintPlugin.class);
    private static final Paper[] paperSizes = new Paper[]{Paper.A0, Paper.A1, Paper.A2, Paper.A3, Paper.A4, Paper.A5, Paper.A6, Paper.A7, Paper.A8, Paper.A9};
    private final PdfPrinter printer = new PdfPrinter();
    private final Spooler spooler;

    private class PdfPrinter implements Printer {

        public String getId() {
            return "PdfPlugin_" + getName();
        }

        public String getName() {
            return "PdfPrinter";
        }

        public String getDescription() {
            return "Generates a PDF from the print.";
        }

        public boolean supportsColor() {
            return true;
        }

        public Paper[] getSupportedPapers() {
            return paperSizes;
        }

        public Margin getPhysicalMargin(Paper paperSize) {
            return new Margin(0, 0, 0, 0);
        }

        public PrinterState getState() {
            return PrinterState.Idle;
        }
    }

    public PdfPrintPlugin(Spooler spooler) {
        this.spooler = spooler;
    }

    public Printer getDefaultPrinter() {
        return printer;
    }

    public List<Printer> getPrinters() {
        List<Printer> list = new ArrayList<Printer>();
        list.add(printer);
        return list;
    }

    public void print(PrinterJob job) {
        try {
            PdfUtil.buildPdf(job, spooler.getOutputStreamForJob(job));
        } catch (DocumentException ex) {
            String message = "Unable to build pdf.";
            LOG.error(message, ex);
            throw new PrinterException(message, ex);
        }

    }
}
