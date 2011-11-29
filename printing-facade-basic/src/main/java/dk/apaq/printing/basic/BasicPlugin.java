package dk.apaq.printing.basic;

import dk.apaq.printing.core.AbstractPrinterManagerPlugin;
import dk.apaq.printing.core.Margin;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterJob;
import dk.apaq.printing.core.util.AWTUtil;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;

/**
 *
 * @author michael
 */
public class BasicPlugin extends AbstractPrinterManagerPlugin {

    private final Printer defaultPrinter;;
    private List<Printer> printers = new ArrayList<Printer>();

    private class PrinterJobPrintable implements Printable {
        private final PrinterJob job;

        public PrinterJobPrintable(PrinterJob job) {
            this.job = job;
        }

        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if(pageIndex>=job.getNumberOfPages()) {
                return NO_SUCH_PAGE;
            }

            job.render((Graphics2D) graphics,pageIndex);
            return PAGE_EXISTS;
        }


    }

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

    @Override
    public Printer getDefaultPrinter() {
        return defaultPrinter;
    }

    @Override
    public List<Printer> getPrinters() {
        return Collections.unmodifiableList(printers);
    }

    @Override
    public void print(PrinterJob job) {
        try {
            BasicPrinter printer = (BasicPrinter) job.getPrinter();
            PageFormat pf = AWTUtil.generatePageformat(job.getPaper(), job.getMargin(), job.getOrientation());
            Paper paper = job.getPaper();
            Margin margin = job.getMargin();

            PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
            attributes.add(MediaSize.findMedia(((Double)paper.getWidth()).floatValue(),
                                                ((Double)paper.getHeight()).floatValue(),
                                                MediaSize.MM));
            attributes.add(AWTUtil.getPrintableArea(paper, margin));
            
            if(job.getDataType() == PrinterJob.DataType.Postscript) {
                DocPrintJob docPrintJob = printer.getAwtPrintService().createPrintJob();
                Doc doc = new SimpleDoc(job.getData(), DocFlavor.BYTE_ARRAY.POSTSCRIPT, null);
                docPrintJob.print(doc, attributes);
            } else {
                PrinterJobPrintable printable = new PrinterJobPrintable(job);
                java.awt.print.PrinterJob awtJob = java.awt.print.PrinterJob.getPrinterJob();
                awtJob.setPrintService(printer.getAwtPrintService());
                awtJob.setPrintable(printable, pf);
                awtJob.print(attributes);
            }
        } catch (PrinterException ex) {
            throw new dk.apaq.printing.core.PrinterException(ex);
        } catch (PrintException ex) {
            throw new dk.apaq.printing.core.PrinterException(ex);
        }

    }

    

}
