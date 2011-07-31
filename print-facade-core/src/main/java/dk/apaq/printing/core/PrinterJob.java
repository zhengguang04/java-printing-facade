package dk.apaq.printing.core;

import java.awt.image.BufferedImage;
import java.awt.print.Pageable;
import java.awt.print.Printable;

/**
 *
 * @author michael
 */
public class PrinterJob<T> {

    private final Printer printer;
    private final T data;
    private int copies;
    private Paper paper;
    private boolean color;

    private PrinterJob(Printer printer, T data) {
        this.printer = printer;
        this.data = data;
    }

    public int getCopies() {
        return copies;
    }

    public void setCopies(int copies) {
        this.copies = copies;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public Paper getPaper() {
        return paper;
    }

    public void setPaper(Paper paper) {
        this.paper = paper;
    }

    public Printer getPrinter() {
        return printer;
    }

    public Object getData() {
        return data;
    }

    public static PrinterJob<String> createJob(Printer printer, String data) {
        return new PrinterJob<String>(printer, data);
    }

    public static PrinterJob<BufferedImage> createJob(Printer printer, BufferedImage data) {
        return new PrinterJob<BufferedImage>(printer, data);
    }

    public static PrinterJob<Printable> createJob(Printer printer, Printable data) {
        return new PrinterJob<Printable>(printer, data);
    }

    public static PrinterJob<Pageable> createJob(Printer printer, Pageable data) {
        return new PrinterJob<Pageable>(printer, data);
    }
}
