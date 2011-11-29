package dk.apaq.printing.ps;

import dk.apaq.printing.core.AbstractPrinterManagerPlugin;
import dk.apaq.printing.core.Margin;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterException;
import dk.apaq.printing.core.PrinterJob;
import dk.apaq.printing.core.PrinterState;
import dk.apaq.printing.core.util.ImageUtil;
import dk.apaq.printing.core.util.PsUtil;
import dk.apaq.printing.core.util.Spooler;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author krog
 */
public class PostscriptPrintPlugin extends AbstractPrinterManagerPlugin {

    private static final Logger LOG = LoggerFactory.getLogger(PostscriptPrintPlugin.class);
    private static final Paper[] paperSizes = new Paper[]{Paper.A0, Paper.A1, Paper.A2, Paper.A3, Paper.A4, Paper.A5, Paper.A6, Paper.A7, Paper.A8, Paper.A9};
    private final PostscriptPrinter printer = new PostscriptPrinter();
    private final Spooler spooler;
    private final int dpi;
   
    private class PostscriptPrinter implements Printer {

        public String getId() {
            return "PostscriptPlugin_" + getName();
        }

        public String getName() {
            return "PostscriptPrinter";
        }

        public String getDescription() {
            return "Generates an postscript file from the print.";
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

    public PostscriptPrintPlugin(Spooler spooler) {
        this.spooler = spooler;
        this.dpi = 300;
    }
    
    public PostscriptPrintPlugin(Spooler spooler, int dpi) {
        this.spooler = spooler;
        this.dpi = dpi;
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
        PsUtil.buildPostscript(job, spooler.getOutputStreamForJob(job));
    }
    
}
