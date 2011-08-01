package dk.apaq.printing.basic;

import dk.apaq.printing.core.AbstractPrinterManagerPlugin;
import dk.apaq.printing.core.Margin;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterJob;
import dk.apaq.printing.core.PrinterListChangeListener;
import java.awt.image.BufferedImage;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.Attribute;
import javax.print.attribute.standard.Chromaticity;
import javax.print.attribute.standard.ColorSupported;

/**
 *
 * @author michael
 */
public class BasicPlugin extends AbstractPrinterManagerPlugin {

    private final Printer defaultPrinter;;
    private List<Printer> printers = new ArrayList<Printer>();

    public BasicPlugin() {
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        for(PrintService ps : services) {
            printers.add(new BasicPrinter(ps));
        }

        PrintService defaultPrintService = PrintServiceLookup.lookupDefaultPrintService();
        if(defaultPrintService!=null) {
            defaultPrinter = new BasicPrinter(defaultPrintService);
        } else {
            defaultPrinter = null;
        }
    }

    public Printer getDefaultPrinter() {
        return defaultPrinter;
    }

    public List<Printer> getPrinters() {
        return Collections.unmodifiableList(printers);
    }

    public void print(PrinterJob job) {
        java.awt.print.PrinterJob awtJob = java.awt.print.PrinterJob.getPrinterJob();

        if(job.getData() instanceof Printable) {
            awtJob.setPrintable((Printable) job.getData());
        }

        if(job.getData() instanceof Pageable) {
            awtJob.setPageable((Pageable) job.getData());
        }

        if(job.getData() instanceof BufferedImage) {
            //awtJob.setPageable((Pageable) job.getData());
        }

        if(job.getData() instanceof String) {
            //awtJob.setPageable((Pageable) job.getData());
        }

        



    }

    

}
