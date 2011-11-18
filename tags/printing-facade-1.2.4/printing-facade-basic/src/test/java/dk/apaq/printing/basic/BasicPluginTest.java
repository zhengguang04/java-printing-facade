package dk.apaq.printing.basic;

import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.PrinterJob;
import dk.apaq.printing.core.PrinterManager;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
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
    public void testPlugin() throws IOException {
        System.out.println("plugin");
        BasicPlugin plugin = new BasicPlugin();

        assertEquals(plugin.getPrinters().size(), PrintServiceLookup.lookupPrintServices(null, null).length);

        PrinterManager manager = new PrinterManager();
        manager.addPlugin(plugin);

        BufferedImage img = ImageIO.read(getClass().getResourceAsStream("/elephant.jpg"));
        PrinterJob job = PrinterJob.getBuilder(manager.getDefaultPrinter(), img).
                setPaper(Paper.A4).
                build();
        //manager.print(job);
        
    }

}
