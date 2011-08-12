package dk.apaq.printfacade.remoteclient;

import dk.apaq.printing.core.Margin;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterJob;
import dk.apaq.printing.core.PrinterManager;
import dk.apaq.printing.core.PrinterState;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
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
public class RemoteClientPluginTest {
    
    public RemoteClientPluginTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }
    
    @Before
    public void setUp() {
        printerManager.addPlugin(clientPlugin);
        
        Map<Paper, Margin> paperMap = new HashMap<Paper, Margin>();
        RemoteClientPrinter clientPrinter = new RemoteClientPrinter("id", "name", "decr", true, paperMap, PrinterState.Idle);
        
        List<RemoteClientPrinter> printers = new ArrayList<RemoteClientPrinter>();
        printers.add(clientPrinter);
        
        clientPlugin.setPrinters(printers);
        clientPlugin.setDefaultPrinterId("id");
    }
    
    @After
    public void tearDown() {
    }

    MockTransport transport = new MockTransport();
    RemoteClientPlugin clientPlugin = new RemoteClientPlugin(transport);
    PrinterManager printerManager = new PrinterManager();
    
    /**
     * Test of getDefaultPrinter method, of class RemoteClientPlugin.
     */
    @Test
    public void testGetDefaultPrinter() throws IOException {
        System.out.println("getDefaultPrinter");
        
        
        Printer printer = printerManager.getDefaultPrinter();
        assertNotNull(printer);
        
    }

    /**
     * Test of getPrinters method, of class RemoteClientPlugin.
     */
   /* @Test
    public void testGetPrinters() {
        System.out.println("getPrinters");
        RemoteClientPlugin instance = null;
        List expResult = null;
        List result = instance.getPrinters();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }*/

    /**
     * Test of print method, of class RemoteClientPlugin.
     */
    @Test
    public void testPrint() throws IOException {
        System.out.println("print");
        
        BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/elephant.jpg"));
        
        Printer printer = printerManager.getDefaultPrinter();
        PrinterJob job = PrinterJob.getBuilder(printer, img).build();
        printerManager.print(job);
    }



}
