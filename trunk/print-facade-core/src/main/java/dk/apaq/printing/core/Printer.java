package dk.apaq.printing.core;

/**
 *
 * @author michael
 */
public interface Printer {

    String getId();
    String getName();
    String description();

    boolean supportsColor();
    PaperSize[] getSupportedPaperSizes();
    PrintableArea getPrintableArea(PaperSize paperSize);

    PrinterState getState();

}
