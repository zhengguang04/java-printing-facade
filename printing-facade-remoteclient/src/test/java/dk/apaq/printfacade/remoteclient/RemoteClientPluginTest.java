package dk.apaq.printfacade.remoteclient;

import dk.apaq.printing.core.PrinterException;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
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

    private class HighResImagePrinter implements Printable {

        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            try {
                if (pageIndex > 0) {
                    return Printable.NO_SUCH_PAGE;
                }

                Graphics2D g2d = (Graphics2D) graphics;
                g2d.setColor(Color.black);

                g2d.drawRect((int) pageFormat.getImageableX(),
                        (int) pageFormat.getImageableY(),
                        (int) pageFormat.getImageableWidth(),
                        (int) pageFormat.getImageableHeight());
                
                int margin = (int)(pageFormat.getWidth()/10);
                int x1 = (int)pageFormat.getImageableX() + margin;
                int y1 = (int)pageFormat.getImageableY() + margin;
                int x2 = (int) pageFormat.getImageableWidth() - (margin * 2);
                int y2 = (int) pageFormat.getImageableHeight() - (margin * 2);
                g2d.fillRect(x1, y1, x2, y2);

                BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/highres_image.jpg"));
                //Set us to the upper left corner
                g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
                AffineTransform at = new AffineTransform();
                at.translate(0, 0);

                //We need to scale the image properly so that it fits on one page.
                double xScale = pageFormat.getImageableWidth() / image.getWidth();
                double yScale = pageFormat.getImageableHeight() / image.getHeight();
                // Maintain the aspect ratio by taking the min of those 2 factors and using it to scale both dimensions.
                double aspectScale = Math.min(xScale, yScale);
                at.setToScale(aspectScale, aspectScale);
                g2d.drawRenderedImage(image, at);

                return Printable.PAGE_EXISTS;

            } catch (IOException ex) {
                throw new PrinterException(ex.getMessage());
            }
        }
    }
    
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
        
        //BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/highres_image.jpg"));
        
        Printer printer = printerManager.getDefaultPrinter();
        PrinterJob job = PrinterJob.getBuilder(printer, new HighResImagePrinter()).setPaper(Paper.A5).build();
        printerManager.print(job);
    }



}
