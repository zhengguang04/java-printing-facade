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
    Paper[] getSupportedPapers();
    Margin getPhysicalMargin(Paper paperSize);

    PrinterState getState();

}
