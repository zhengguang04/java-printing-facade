package dk.apaq.printfacade.googlecloud;

import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterJob;
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
        
        //GoogleCloudPrintPlugin.AccessTokenInfo info = 
        //        new GoogleCloudPrintPlugin.AccessTokenInfo("ya29.AHES6ZQAhEIBJneie8UmW7FVBdsa2B0-Di2qtMxEcRRC8zU", 
        //                                                  3600, "Bearer", "1/nRk3wSocUnHMUSB1KkbsC-zZyExAi34iJKm_uG6fdxE");
        cloudPrintPlugin = new GoogleCloudPrintPlugin(new ClientLoginAuthorizer("user@gmail.com", "password", "test"), "test");
//        cloudPrintPlugin = new GoogleCloudPrintPlugin(clientid, clientSecret, code);
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
    public void testGetPrinters() {
    System.out.println("getPrinters");
    List result = cloudPrintPlugin.getPrinters();
    assertNotNull(result);
    
    }
     
    /**
     * Test of print method, of class GoogleCloudPrintPlugin.
     */
    /*@Test
    public void testPrint() {
    System.out.println("print");
    PrinterJob job = null;
    GoogleCloudPrintPlugin instance = null;
    instance.print(job);
    // TODO review the generated test code and remove the default call to fail.
    fail("The test case is a prototype.");
    }*/
}
