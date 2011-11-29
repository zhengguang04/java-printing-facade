package dk.apaq.printing.image;

import dk.apaq.printing.core.AbstractPrinterManagerPlugin;
import dk.apaq.printing.core.Margin;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterException;
import dk.apaq.printing.core.PrinterJob;
import dk.apaq.printing.core.PrinterJob.DataType;
import dk.apaq.printing.core.PrinterState;
import dk.apaq.printing.core.util.ImageUtil;
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
public class ImagePrintPlugin extends AbstractPrinterManagerPlugin {

    private static final Logger LOG = LoggerFactory.getLogger(ImagePrintPlugin.class);
    private static final Paper[] paperSizes = new Paper[]{Paper.A0, Paper.A1, Paper.A2, Paper.A3, Paper.A4, Paper.A5, Paper.A6, Paper.A7, Paper.A8, Paper.A9};
    private final ImagePrinter printer = new ImagePrinter();
    private final Spooler spooler;
    private final int dpi;
   
    private class ImagePrinter implements Printer {

        public String getId() {
            return "ImagePlugin_" + getName();
        }

        public String getName() {
            return "ImagePrinter";
        }

        public String getDescription() {
            return "Generates an image from the print.";
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

        public boolean supportDatatype(DataType dataType) {
            switch(dataType) {
                case Pageable:
                    return true;
                default:
                    return false;
            }
        }
        
        
    }

    public ImagePrintPlugin(Spooler spooler) {
        this.spooler = spooler;
        this.dpi = 300;
    }
    
    public ImagePrintPlugin(Spooler spooler, int dpi) {
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
        try {
            ImageUtil.buildImage(job, dpi, spooler.getOutputStreamForJob(job));
        } catch (IOException ex) {
            String message = "Unable to build zip with images.";
            LOG.error(message, ex);
            throw new PrinterException(message, ex);
        }

    }
    
    
    
}
