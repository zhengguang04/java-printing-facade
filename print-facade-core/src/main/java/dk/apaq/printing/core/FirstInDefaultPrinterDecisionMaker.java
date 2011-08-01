package dk.apaq.printing.core;

/**
 *
 * @author michael
 */
public class FirstInDefaultPrinterDecisionMaker implements DefaultPrinterDecisionMaker{

    public Printer getDefaultPrinter(PrinterManager manager) {
        for(int i=0;i<manager.getPluginCount();i++) {
            PrinterManagerPlugin plugin = manager.getPlugin(i);
            Printer printer = plugin.getDefaultPrinter();
            if(printer!=null) {
                return printer;
            }
        }
        return null;
    }

}
