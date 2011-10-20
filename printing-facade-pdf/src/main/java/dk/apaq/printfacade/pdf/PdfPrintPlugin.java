package dk.apaq.printfacade.pdf;

import com.itextpdf.text.DocumentException;
import dk.apaq.printing.core.AbstractPrinterManagerPlugin;
import dk.apaq.printing.core.Margin;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterJob;
import dk.apaq.printing.core.PrinterState;
import dk.apaq.printing.core.util.PdfUtil;
import java.awt.Window;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author krog
 */
public class PdfPrintPlugin extends AbstractPrinterManagerPlugin  {

    private static final Logger LOG = LoggerFactory.getLogger(PdfPrintPlugin.class);
    private static final Paper[] paperSizes = new Paper[]{Paper.A0, Paper.A1, Paper.A2, Paper.A3, Paper.A4, Paper.A5, Paper.A6, Paper.A7, Paper.A8, Paper.A9};
    private final PdfPrinter printer = new PdfPrinter();
    private final Spooler spooler;
    
    public interface Spooler {
        OutputStream getOutputStreamForJob(PrinterJob job);
    }
    
    public interface InMemorySpoolerHandler {
        void onSpool(PrinterJob job, byte[] data);
    }
    
    private static class InMemorySpoolerOutputStream extends ByteArrayOutputStream {
    
        private final PrinterJob job;
        private final InMemorySpoolerHandler handler;

        public InMemorySpoolerOutputStream(PrinterJob job, InMemorySpoolerHandler handler) {
            this.job = job;
            this.handler = handler;
        }

        @Override
        public void close() throws IOException {
            super.close();
            handler.onSpool(job, toByteArray());
        }
        
        
    }
    
    public static class InMemorySpooler implements Spooler {

        private final InMemorySpoolerHandler handler;

        public InMemorySpooler(InMemorySpoolerHandler handler) {
            this.handler = handler;
        }
        
        public OutputStream getOutputStreamForJob(PrinterJob job) {
            return new InMemorySpoolerOutputStream(job, handler);
        }
    }
    
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
            LOG.error("Unable to build pdf.", ex);
        }

    }
    
}
