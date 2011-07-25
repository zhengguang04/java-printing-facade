package dk.apaq.printing.core;

import dk.apaq.printing.core.abilities.PaperSize;
import dk.apaq.printing.core.abilities.PrintableArea;

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
