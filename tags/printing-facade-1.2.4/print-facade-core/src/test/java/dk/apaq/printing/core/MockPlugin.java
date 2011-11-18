package dk.apaq.printing.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author michaelzachariassenkrog
 */
public class MockPlugin extends AbstractPrinterManagerPlugin {

    private final List<Printer> printers = new ArrayList<Printer>();
    private PrinterJob lastJob;
    
    public MockPlugin() {
    }

    public MockPlugin(Printer ... printers) {
        this.printers.addAll(Arrays.asList(printers));
    }

    public Printer getDefaultPrinter() {
        if(printers.isEmpty()) {
            return null;
        } else {
            return printers.get(0);
        }
    }



    public List<Printer> getPrinters() {
        return Collections.unmodifiableList(printers);
    }

    public void print(PrinterJob job) {
        this.lastJob = job;

        
    }


    public PrinterJob getLastJob() {
        return lastJob;
    }

    public void addPrinter(Printer printer) {
        printers.add(printer);
        fireChangeEvent();
    }

    public int getPrinterCount() {
        return printers.size();
    }

    public Printer getPrinter(int index) {
        return printers.get(index);
    }

    public void removePrinter(int index) {
        printers.remove(index);
        fireChangeEvent();
    }



}
