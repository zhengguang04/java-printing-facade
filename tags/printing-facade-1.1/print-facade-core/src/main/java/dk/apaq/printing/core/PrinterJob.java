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

    private final Printer printer;
    private Pageable pageable;
    private final Object data;
    private int copies;
    private Paper paper = Paper.A4;
    private Margin margin = new Margin(0, 0, 0, 0);
    private Orientation orientation = Orientation.Portrait;
    private boolean color;

    private static class SimplePageable implements Pageable {

        private final Printable printable;
        private final int numberOfPages;
        private final Paper paper;
        private final Margin margin;
        private final Orientation orientation;

        public SimplePageable(Printable printable, Paper paper, Margin margin, Orientation orientation) {
            this.printable = printable;
            this.paper = paper;
            this.numberOfPages = -1;
            this.margin = margin;
            this.orientation = orientation;
        }

        public SimplePageable(Printable printable, Paper paper, Margin margin, Orientation orientation, int numberOfPages) {
            this.printable = printable;
            this.numberOfPages = numberOfPages;
            this.paper = paper;
            this.margin = margin;
            this.orientation = orientation;
        }


        public int getNumberOfPages() {
            return numberOfPages;
        }

        public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
            return AWTUtil.generatePageformat(paper, margin, orientation);
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

    private PrinterJob(Printer printer, Object data) {
        this.printer = printer;
        this.data = data;
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

    public int getNumberOfPages() {
        ensurePageableExists();
        return pageable.getNumberOfPages();
    }

    public void render(Graphics2D gfx, int pageNumber) {
        ensurePageableExists();
        doRenderPageable(gfx, copies, pageable);
    }

    public static PrinterJobBuilder getBuilder(Printer printer, String text) {
        return new PrinterJobBuilder(new PrinterJob(printer, text));
    }

    public static PrinterJobBuilder getBuilder(Printer printer, BufferedImage image) {
        return new PrinterJobBuilder(new PrinterJob(printer, image));
    }

    public static PrinterJobBuilder getBuilder(Printer printer, Printable printable) {
        return new PrinterJobBuilder(new PrinterJob(printer, printable));
    }

    public static PrinterJobBuilder getBuilder(Printer printer, Pageable pageable) {
        return new PrinterJobBuilder(new PrinterJob(printer, pageable));
    }

    private void ensurePageableExists() {
        if(pageable!=null) {
            return;
        }

        if(data instanceof Pageable) {
            pageable = (Pageable) data;
            return;
        }

        if(data instanceof BufferedImage) {
            Printable printable = new ImagePrintable((BufferedImage) data);
            pageable = new SimplePageable(printable, paper, margin, orientation, 1);
            return;
        }

        if(data instanceof Printable) {
            pageable = new SimplePageable((Printable) data,paper, margin, orientation);
            return;
        }

    }

    private void doRenderPageable(Graphics2D gfx, int page, Pageable pageable) {
        try {
            PageFormat format = pageable.getPageFormat(page);
            Printable printable = pageable.getPrintable(page);
            printable.print(gfx, format, page);
        } catch (PrinterException ex) {
            throw new dk.apaq.printing.core.PrinterException(ex);
        }
    }
}
