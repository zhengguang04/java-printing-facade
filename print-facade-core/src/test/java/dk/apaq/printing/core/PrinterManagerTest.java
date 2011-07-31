/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.printing.core;

import java.awt.image.BufferedImage;
import junit.framework.TestCase;

/**
 *
 * @author michaelzachariassenkrog
 */
public class PrinterManagerTest extends TestCase {
    
    public PrinterManagerTest(String testName) {
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

    /**
     * Test of addPlugin method, of class PrinterManager.
     */
    public void testAddRemovePlugin() {
        System.out.println("addPlugin");
        Paper[] papers = new Paper[]{Paper.A4};
        Printer[] printers = new Printer[]{new MockPrinter("test", "test", "", true, papers)};
        PrinterManagerPlugin plugin = new MockPlugin(printers);
        PrinterManager instance = new PrinterManager();
        instance.addPlugin(plugin);

        assertEquals(1, instance.getPluginCount());
        assertEquals(plugin, instance.getPlugin(0));

        instance.removePlugin(plugin);

        assertEquals(0, instance.getPluginCount());
    }


    /**
     * Test of print method, of class PrinterManager.
     */
    public void testPrint() {
        System.out.println("print");

        Paper[] papers = new Paper[]{Paper.A4};
        Printer[] printers = new Printer[]{new MockPrinter("test", "test", "", true, papers)};
        MockPlugin plugin = new MockPlugin(printers);
        PrinterManager instance = new PrinterManager();
        instance.addPlugin(plugin);

        BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_ARGB);
        img.createGraphics().drawString("Tes test", 20, 20);
        PrinterJob job = PrinterJob.createJob(instance.getPrinters().get(0), img);

        instance.print(job);
        assertNotNull(plugin.getLastJob());

    }

    public void testAddListener() {
        System.out.println("addListener");
        PrinterListChangeListener listener = null;
        PrinterManager instance = new PrinterManager();
        instance.addListener(listener);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    public void testRemoveListener() {
        System.out.println("removeListener");
        PrinterListChangeListener listener = null;
        PrinterManager instance = new PrinterManager();
        instance.removeListener(listener);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
