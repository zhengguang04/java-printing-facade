package dk.apaq.printing.pdf;

import dk.apaq.printing.pdf.PdfPrintPlugin;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterJob;
import dk.apaq.printing.core.util.InMemorySpooler;
import dk.apaq.printing.core.util.Spooler;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.List;
import junit.framework.TestCase;

/**
 *
 * @author krog
 */
public class PdfPrintPluginTest extends TestCase {
    
    public PdfPrintPluginTest(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private MockSpoolerHandler spoolerHandler = new MockSpoolerHandler();
    private Spooler spooler = new InMemorySpooler(spoolerHandler);
    private PdfPrintPlugin pdfPrintPlugin = new PdfPrintPlugin(spooler);
    /**
     * Test of print method, of class PdfPrintPlugin.
     */
    public void testPrint() {
        System.out.println("print");
        PrinterJob job = PrinterJob.getBuilder(pdfPrintPlugin.getDefaultPrinter(), new Printable() {

            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if(pageIndex>0) return NO_SUCH_PAGE;
                graphics.drawString("Dette er en test.", 100, 100);
                return PAGE_EXISTS;
            }
        }).build();
        pdfPrintPlugin.print(job);
        
        assertNotNull(spoolerHandler.getLastData());
        assertNotNull(spoolerHandler.getLastJob());
    }
}
