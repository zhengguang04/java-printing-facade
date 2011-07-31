package dk.apaq.printing.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author michaelzachariassenkrog
 */
public class MockPlugin implements PrinterManagerPlugin {

    private final Printer[] printers;
    private PrinterJob lastJob;
    private List<PrinterListChangeListener> listeners = new ArrayList<PrinterListChangeListener>();

    public MockPlugin(Printer ... printers) {
        this.printers = printers;
    }


    public List<Printer> getPrinters() {
        return Arrays.asList(printers);
    }

    public void print(PrinterJob job) {
        this.lastJob = job;
    }

    public void addListener(PrinterListChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(PrinterListChangeListener listener) {
        listeners.remove(listener);
    }

    public PrinterJob getLastJob() {
        return lastJob;
    }

}
