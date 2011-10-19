package dk.apaq.printfacade.googlecloud;

import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterJob;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.util.Date;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author krog
 */
public class GoogleCloudPrintPluginTest {

    public GoogleCloudPrintPluginTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        String code = "4/BAGrNuYZWXdQO5XK9cH74NOhS6JC";
        String clientid = "700939733854.apps.googleusercontent.com";
        String clientSecret = "TwM1ADz1REuRfd5muU89Rejv";

        cloudPrintPlugin = new GoogleCloudPrintPlugin(new ClientLoginAuthorizer("michael.krog@gmail.com", "joojoo2010", "test"), "test");

    }

    @After
    public void tearDown() {
    }
    private GoogleCloudPrintPlugin cloudPrintPlugin;

    /**
     * Test of getDefaultPrinter method, of class GoogleCloudPrintPlugin.
     */
    /*@Test
    public void testGetDefaultPrinter() {
    System.out.println("getDefaultPrinter");
    Printer result = cloudPrintPlugin.getDefaultPrinter();
    //assertEquals(expResult, result);
    }*/
    /**
     * Test of getPrinters method, of class GoogleCloudPrintPlugin.
     */
    @Test
    public void testGetPrinters() throws InterruptedException {
        System.out.println("getPrinters");
        /*List result = cloudPrintPlugin.getPrinters();
        
        Thread.sleep(20000);
        result = cloudPrintPlugin.getPrinters();
        
        assertFalse(result.isEmpty());
         */
    }

    /**
     * Test of print method, of class GoogleCloudPrintPlugin.
     */
    @Test
    public void testPrint() {
        System.out.println("print");

        Printer printer = null;
        for (Printer current : cloudPrintPlugin.getPrinters()) {
            if (current.getId().equals("__google__docs")) {
                printer = current;
                break;
            }
        }

        PrinterJob job = PrinterJob.getBuilder(printer, new Printable() {

            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return NO_SUCH_PAGE;
                }

                graphics.drawString("Hallo hallo", 100, 100);
                return PAGE_EXISTS;
            }
        }).build();
        
        cloudPrintPlugin.print(job);

    }
}
