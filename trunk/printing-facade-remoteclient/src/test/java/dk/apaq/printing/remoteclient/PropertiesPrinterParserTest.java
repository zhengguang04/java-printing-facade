package dk.apaq.printing.remoteclient;

import dk.apaq.printing.remoteclient.PropertiesPrinterParser;
import dk.apaq.printing.core.Margin;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterState;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author michaelzachariassenkrog
 */
public class PropertiesPrinterParserTest {

    public PropertiesPrinterParserTest() {
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

    private String data = "printers.count=1\n" +
                            "printers.0.id=id\n" +
                            "printers.0.name=name\n" +
                            "printers.0.description=description\n" +
                            "printers.0.colorSupported=true\n" +
                            "printers.0.state=Idle\n" +
                            "printers.0.papers.count=1\n" +
                            "printers.0.papers.0.size=210mm x 297mm\n" +
                            "printers.0.papers.0.margin=5mm, 10mm, 5mm, 10mm\n";


    /**
     * Test of getPrinterCount method, of class PropertiesPrinterParser.
     */
    @Test
    public void testParser() throws IOException {
        Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(data.getBytes()));
        PropertiesPrinterParser parser = new PropertiesPrinterParser(properties);

        assertEquals(1, parser.getPrinterCount());
        assertEquals("id", parser.getPrinter(0).getId());
        assertEquals("name", parser.getPrinter(0).getName());
        //assertEquals("description", parser.getPrinter(0).get());
        assertEquals(PrinterState.Idle, parser.getPrinter(0).getState());

        assertEquals(1, parser.getPrinter(0).getSupportedPapers().length);
        assertEquals(Paper.A4, parser.getPrinter(0).getSupportedPapers()[0]);
        assertEquals(new Margin(5,10, 5, 10), parser.getPrinter(0).getPhysicalMargin(Paper.A4));



    }

    

}