package dk.apaq.printing.image;

import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.PrinterJob;
import dk.apaq.printing.core.util.InMemorySpooler;
import dk.apaq.printing.core.util.Spooler;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;
import junit.framework.TestCase;

/**
 *
 * @author krog
 */
public class ImagePrintPluginTest extends TestCase {
    
    public ImagePrintPluginTest(String testName) {
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
    private ImagePrintPlugin pdfPrintPlugin = new ImagePrintPlugin(spooler);
    /**
     * Test of print method, of class PdfPrintPlugin.
     */
    public void testPrint() throws IOException {
        System.out.println("print");
        PrinterJob job = PrinterJob.getBuilder(pdfPrintPlugin.getDefaultPrinter(), new Printable() {

            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if(pageIndex>0) return NO_SUCH_PAGE;
                graphics.drawString("Dette er en test.", 100, 100);
                return PAGE_EXISTS;
            }
        }).setPaper(Paper.A4).build();
        pdfPrintPlugin.print(job);
        
        assertNotNull(spoolerHandler.getLastData());
        assertNotNull(spoolerHandler.getLastJob());
        
        ZipInputStream zin = new ZipInputStream(new ByteArrayInputStream(spoolerHandler.getLastData()));
        assertTrue(zin.available()==1);
        
        ZipEntry entry = zin.getNextEntry();
        if(!entry.getName().endsWith(".png")) {
            entry = zin.getNextEntry();
        }
        
        if(!entry.getName().endsWith(".png")) {
            fail("No images in zipfile.");
        }
        
        BufferedImage img = ImageIO.read(zin);
        assertEquals(Paper.A4.getWidth() / 10 / 2.54 * 300, img.getWidth(), 0.5);
        zin.close();
    }
}
