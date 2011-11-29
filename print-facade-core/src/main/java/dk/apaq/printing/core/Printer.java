package dk.apaq.printing.core;

/**
 *
 * @author michael
 */
public interface Printer {

    String getId();
    String getName();
    String getDescription();

    boolean supportsColor();
    boolean supportDatatype(PrinterJob.DataType dataType);
    Paper[] getSupportedPapers();
    Margin getPhysicalMargin(Paper paperSize);

    PrinterState getState();

}
