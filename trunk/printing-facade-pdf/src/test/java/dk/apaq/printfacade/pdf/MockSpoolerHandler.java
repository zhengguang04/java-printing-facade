package dk.apaq.printfacade.pdf;

import dk.apaq.printfacade.pdf.PdfPrintPlugin;
import dk.apaq.printing.core.PrinterJob;

/**
 *
 * @author krog
 */
public class MockSpoolerHandler implements PdfPrintPlugin.InMemorySpoolerHandler {

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
