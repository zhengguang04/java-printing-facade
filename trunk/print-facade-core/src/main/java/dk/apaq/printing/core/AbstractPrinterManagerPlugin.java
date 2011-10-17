package dk.apaq.printing.core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author michael
 */
public abstract class AbstractPrinterManagerPlugin implements PrinterManagerPlugin {

    private List<PrinterListChangeListener> listeners = new ArrayList<PrinterListChangeListener>();

    @Override
    public void addListener(PrinterListChangeListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(PrinterListChangeListener listener) {
        listeners.remove(listener);
    }

    protected void fireChangeEvent() {
        PrinterEvent e = new PrinterEvent(this);
        for(PrinterListChangeListener listener : listeners) {
            listener.onPrinterListChange(e);
        }
    }


}
