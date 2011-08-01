/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import dk.apaq.printing.basic.BasicPlugin;
import javax.print.PrintServiceLookup;
import junit.framework.TestCase;
import org.junit.Test;

/**
 *
 * @author michael
 */
public class BasicPluginTest extends TestCase {
    
    public BasicPluginTest(String testName) {
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

    @Test
    public void testPlugin() {
        System.out.println("plugin");
        BasicPlugin plugin = new BasicPlugin();

        assertEquals(plugin.getPrinters().size(), PrintServiceLookup.lookupPrintServices(null, null).length);
    }

}
