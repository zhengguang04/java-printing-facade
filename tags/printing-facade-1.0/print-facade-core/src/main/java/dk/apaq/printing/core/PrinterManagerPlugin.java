package dk.apaq.printing.core;

import java.util.List;

/**
 *
 * @author michael
 */
public interface PrinterManagerPlugin extends PrinterListChangeNotifier{

    public Printer getDefaultPrinter();
    public List<Printer> getPrinters();
    public void print(PrinterJob job);
}
