package dk.apaq.printing.core;

import dk.apaq.printing.core.util.AWTUtil;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

/**
 *
 * @author michael
 */
public class PrinterJob {

    private static int jobCount=0;
    
    private final Printer printer;
    private final Pageable pageable;
    private final byte[] data;
    private int copies = 1;
    private Paper paper = Paper.A4;
    private Margin margin = new Margin(0, 0, 0, 0);
    private Orientation orientation = Orientation.Portrait;
    private boolean color;
    private String name = "PrinterJob-" + jobCount++;
    private final DataType dataType;

    public enum DataType {
        Pageable, Postscript
    }
    
    private static class SimplePageable implements Pageable {

        private final Printable printable;
        private final int numberOfPages;
        private PrinterJob printerJob;
        
        public SimplePageable(Printable printable) {
            this.printable = printable;
            this.numberOfPages = -1;
        }

        public SimplePageable(Printable printable, int numberOfPages) {
            this.printable = printable;
            this.numberOfPages = numberOfPages;
        }

        private void setPrinterJob(PrinterJob job) {
            this.printerJob = job;
        }

        public int getNumberOfPages() {
            return numberOfPages;
        }

        public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
            return AWTUtil.generatePageformat(printerJob.getPaper(), printerJob.margin, printerJob.orientation);
        }

        public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
            return printable;
        }

    }

    private static class ImagePrintable implements Printable {
        private final BufferedImage image;

        public ImagePrintable(BufferedImage image) {
            this.image = image;
        }

        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if(pageIndex!=0) {
                return NO_SUCH_PAGE;
            }

            Graphics2D g2d = (Graphics2D) graphics;
            AffineTransform at = new AffineTransform();
            at.translate(0, 0);

            //We need to scale the image properly so that it fits on one page.
            double xScale = pageFormat.getWidth() / image.getWidth();
            double yScale = pageFormat.getHeight() / image.getHeight();

            // Maintain the aspect ratio by taking the min of those 2 factors and using it to scale both dimensions.
            double aspectScale = Math.min(xScale, yScale);
            at.setToScale(aspectScale, aspectScale);
            g2d.drawRenderedImage(image, at);

            return Printable.PAGE_EXISTS;
        }


    }

    public static class PrinterJobBuilder {

        private final PrinterJob job;

        public PrinterJobBuilder(PrinterJob job) {
            this.job = job;
        }

        public PrinterJobBuilder setName(String name) {
            job.name = name;
            return this;
        }
        
        public PrinterJobBuilder setColorEnabled(boolean enabled) {
            job.color = enabled;
            return this;
        }

        public PrinterJobBuilder setCopies(int copies) {
            this.job.copies = copies;
            return this;
        }

        public PrinterJobBuilder setPaper(Paper paper) {
            this.job.paper = paper;
            return this;
        }

        public PrinterJobBuilder setMargin(Margin margin) {
            this.job.margin = margin;
            return this;
        }

        public PrinterJobBuilder setOrientation(Orientation orientation) {
            this.job.orientation = orientation;
            return this;
        }

        public PrinterJob build() {
            return job;
        }


    }

    private PrinterJob(Printer printer, Pageable pageable) {
        this.printer = printer;
        this.pageable = pageable;
        this.data = null;
        this.dataType = DataType.Pageable;
    }
    
    private PrinterJob(Printer printer, DataType dataType, byte[] data) {
        this.printer = printer;
        this.data=data;
        this.pageable = null;
        this.dataType = dataType.Postscript;
    }

    public String getName() {
        return name;
    }

    public int getCopies() {
        return copies;
    }

    public boolean isColorEnabled() {
        return color;
    }

    public Paper getPaper() {
        return paper;
    }

    public Margin getMargin() {
        return margin;
    }

    public Orientation getOrientation() {
        return orientation;
    }

    public Printer getPrinter() {
        return printer;
    }

    /**
     * Returns the number of pages or -1 if number of pages is unknown in advance.
     */
    public int getNumberOfPages() {
        return pageable == null ? -1 : pageable.getNumberOfPages();
    }

    public boolean render(Graphics2D gfx, int pageNumber) {
        return pageable == null ? false : doRenderPageable(gfx, pageNumber, pageable);
    }

    /*public static PrinterJobBuilder getBuilder(Printer printer, String text) {
        return new PrinterJobBuilder(new PrinterJob(printer, text));
    }*/

    public static PrinterJobBuilder getBuilder(Printer printer, DataType dataType, byte[] data) {
        PrinterJob job = new PrinterJob(printer, dataType, data);
        return new PrinterJobBuilder(job);
    }
    
    public static PrinterJobBuilder getBuilder(Printer printer, BufferedImage image) {
        Printable printable = new ImagePrintable((BufferedImage) image);
        SimplePageable pageable = new SimplePageable(printable, 1);
        PrinterJob job = new PrinterJob(printer, pageable);
        pageable.setPrinterJob(job);
        return new PrinterJobBuilder(job);
    }

    public static PrinterJobBuilder getBuilder(Printer printer, Printable printable) {
        SimplePageable pageable = new SimplePageable(printable, -1);
        PrinterJob job = new PrinterJob(printer, pageable);
        pageable.setPrinterJob(job);
        return new PrinterJobBuilder(job);
    }

    public static PrinterJobBuilder getBuilder(Printer printer, Pageable pageable) {
        return new PrinterJobBuilder(new PrinterJob(printer, pageable));
    }

    public DataType getDataType() {
        return dataType;
    }

    public byte[] getData() {
        return data;
    }

    private boolean doRenderPageable(Graphics2D gfx, int page, Pageable pageable) {
        try {
            PageFormat format = pageable.getPageFormat(page);
            Printable printable = pageable.getPrintable(page);
            return printable.print(gfx, format, page) == Printable.PAGE_EXISTS;
        } catch (PrinterException ex) {
            throw new dk.apaq.printing.core.PrinterException(ex);
        }
    }
}
