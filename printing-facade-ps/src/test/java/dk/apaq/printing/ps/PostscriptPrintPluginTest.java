package dk.apaq.printing.ps;

import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.PrinterException;
import dk.apaq.printing.core.util.InMemorySpooler;
import dk.apaq.printing.core.util.Spooler;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;
import dk.apaq.printing.core.PrinterJob;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author michael
 */
public class PostscriptPrintPluginTest {
    
    public PostscriptPrintPluginTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    private MockSpoolerHandler spoolerHandler = new MockSpoolerHandler();
    private Spooler spooler = new InMemorySpooler(spoolerHandler);
    private PostscriptPrintPlugin printPlugin = new PostscriptPrintPlugin(spooler);
    /**
     * Test of print method, of class PdfPrintPlugin.
     */
    @Test
    public void testPrint() throws IOException, InterruptedException {
        System.out.println("print");
        PrinterJob job = PrinterJob.getBuilder(printPlugin.getDefaultPrinter(), new Printable() {

            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if(pageIndex>0) return NO_SUCH_PAGE;
                graphics.drawString("Dette er en test.", 100, 100);
                return PAGE_EXISTS;
            }
        }).setPaper(Paper.A4).build();
        printPlugin.print(job);
        
        assertNotNull(spoolerHandler.getLastData());
        assertNotNull(spoolerHandler.getLastJob());
        
        String value = new String(spoolerHandler.getLastData());
        assertNotSame(0, value.length());
        assertTrue(value.contains("PageSize [595.2755737304688 841.8897705078125]"));
        assertTrue(value.contains("Page: 1 1"));
        assertTrue(value.contains("%!PS-Adobe-3.0"));
        
       
    }
}
