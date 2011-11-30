package dk.apaq.printing.core;

import dk.apaq.printing.core.PrinterJob.DataType;
import dk.apaq.printing.core.PrinterJob.PrinterJobBuilder;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.Pageable;
import java.awt.print.Printable;
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
public class PrinterJobTest {
    
    public PrinterJobTest() {
    }

    /**
     * Test of getName method, of class PrinterJob.
     */
    @Test
    public void testGetName() {
        System.out.println("getName");
        Printer printer = new MockPrinter(null, null, null, true, null);
        PrinterJob instance = PrinterJob.getBuilder(printer, PrinterJob.DataType.Postscript, null).
                                setName("test").build();
        
        String expResult = "test";
        String result = instance.getName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCopies method, of class PrinterJob.
     */
    @Test
    public void testGetCopies() {
        System.out.println("getCopies");
        Printer printer = new MockPrinter(null, null, null, true, null);
        PrinterJob instance = PrinterJob.getBuilder(printer, PrinterJob.DataType.Postscript, null).
                                setCopies(3).build();
        
        
        int expResult = 3;
        int result = instance.getCopies();
        assertEquals(expResult, result);
    }

    /**
     * Test of isColorEnabled method, of class PrinterJob.
     */
    @Test
    public void testIsColorEnabled() {
        System.out.println("isColorEnabled");
        Printer printer = new MockPrinter(null, null, null, true, null);
        PrinterJob instance = PrinterJob.getBuilder(printer, PrinterJob.DataType.Postscript, null).
                                setColorEnabled(false).build();
        
        
        boolean expResult = false;
        boolean result = instance.isColorEnabled();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPaper method, of class PrinterJob.
     */
    @Test
    public void testGetPaper() {
        System.out.println("getPaper");
        Printer printer = new MockPrinter(null, null, null, true, null);
        PrinterJob instance = PrinterJob.getBuilder(printer, PrinterJob.DataType.Postscript, null).
                                setPaper(Paper.A0).build();
        
        
        Paper expResult = Paper.A0;
        Paper result = instance.getPaper();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMargin method, of class PrinterJob.
     */
    @Test
    public void testGetMargin() {
        System.out.println("getMargin");
        Margin margin = new Margin(1, 2, 3, 4);
        Printer printer = new MockPrinter(null, null, null, true, null);
        PrinterJob instance = PrinterJob.getBuilder(printer, PrinterJob.DataType.Postscript, null).
                                setMargin(margin).build();
        
        
        Margin expResult = margin;
        Margin result = instance.getMargin();
        assertEquals(expResult, result);
    }

    /**
     * Test of getOrientation method, of class PrinterJob.
     */
    @Test
    public void testGetOrientation() {
        System.out.println("getOrientation");
        Printer printer = new MockPrinter(null, null, null, true, null);
        PrinterJob instance = PrinterJob.getBuilder(printer, PrinterJob.DataType.Postscript, null).
                                setOrientation(Orientation.Portrait).build();
        Orientation expResult = Orientation.Portrait;
        Orientation result = instance.getOrientation();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPrinter method, of class PrinterJob.
     */
    @Test
    public void testGetPrinter() {
        System.out.println("getPrinter");
        Printer printer = new MockPrinter(null, null, null, true, null);
        PrinterJob instance = PrinterJob.getBuilder(printer, PrinterJob.DataType.Postscript, null).
                                build();
        Printer expResult = printer;
        Printer result = instance.getPrinter();
        assertEquals(expResult, result);
    }

    /**
     * Test of getNumberOfPages method, of class PrinterJob.
     */
    @Test
    public void testGetNumberOfPages() {
        /*System.out.println("getNumberOfPages");
        Printer printer = new MockPrinter(null, null, null, true, null);
        PrinterJob instance = PrinterJob.getBuilder(printer, PrinterJob.DataType.Postscript, null).
                                build();
        int expResult = 0;
        int result = instance.getNumberOfPages();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");*/
    }

    /**
     * Test of render method, of class PrinterJob.
     */
    @Test
    public void testRender() {
        /*System.out.println("render");
        Graphics2D gfx = null;
        int pageNumber = 0;
        PrinterJob instance = null;
        boolean expResult = false;
        boolean result = instance.render(gfx, pageNumber);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");*/
    }

   
    /**
     * Test of getDataType method, of class PrinterJob.
     */
    @Test
    public void testGetDataType() {
        System.out.println("getDataType");
        Printer printer = new MockPrinter(null, null, null, true, null);
        PrinterJob instance = PrinterJob.getBuilder(printer, PrinterJob.DataType.Postscript, null).
                                setOrientation(Orientation.Portrait).build();
        DataType expResult = PrinterJob.DataType.Postscript;
        DataType result = instance.getDataType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getData method, of class PrinterJob.
     */
    @Test
    public void testGetData() {
        System.out.println("getData");
        byte[] data = {27};
        Printer printer = new MockPrinter(null, null, null, true, null);
        PrinterJob instance = PrinterJob.getBuilder(printer, PrinterJob.DataType.Postscript, data).
                                setOrientation(Orientation.Portrait).build();
        byte[] expResult = data;
        byte[] result = instance.getData();
        assertEquals(expResult, result);
    }
}
