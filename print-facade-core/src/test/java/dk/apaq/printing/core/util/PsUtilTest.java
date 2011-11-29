package dk.apaq.printing.core.util;

import dk.apaq.printing.core.MockPrinter;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.PrinterJob;
import java.awt.image.BufferedImage;
import java.io.OutputStream;
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
public class PsUtilTest {
    
    public PsUtilTest() {
    }


  

    /**
     * Test of buildPostscript method, of class PsUtil.
     */
    @Test
    public void testBuildPostscript_PrinterJob_boolean() throws Exception {
        System.out.println("buildPostscript");
        Paper[] papers = {Paper.A4};
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        PrinterJob job = PrinterJob.getBuilder(new MockPrinter("12", "21", "sdasd", true, papers), img).build();
        boolean zip = true;
        byte[] result = PsUtil.buildPostscript(job, zip);
        assertNotNull(result);
        
    }
}
