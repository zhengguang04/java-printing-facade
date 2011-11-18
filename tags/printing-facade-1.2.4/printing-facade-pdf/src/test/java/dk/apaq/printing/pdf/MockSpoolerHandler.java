package dk.apaq.printing.pdf;

import dk.apaq.printing.core.PrinterJob;
import dk.apaq.printing.core.util.InMemorySpooler.InMemorySpoolerHandler;

/**
 *
 * @author krog
 */
public class MockSpoolerHandler implements InMemorySpoolerHandler {

    private PrinterJob lastJob;
    private byte[] lastData;
    
    public void onSpool(PrinterJob job, byte[] data) {
        this.lastJob = job;
        this.lastData = data;
    }

    public PrinterJob getLastJob() {
        return lastJob;
    }

    public byte[] getLastData() {
        return lastData;
    }
    
    
    
}
