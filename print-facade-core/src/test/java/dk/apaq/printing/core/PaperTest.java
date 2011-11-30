package dk.apaq.printing.core;

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
public class PaperTest {
    
    public PaperTest() {
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

    /**
     * Test of getHeight method, of class Paper.
     */
    @Test
    public void testGetHeight() {
        System.out.println("getHeight");
        Paper instance = Paper.A4;
        double expResult = 297.0;
        double result = instance.getHeight();
        assertEquals(expResult, result, 0.1);
    }

    /**
     * Test of getWidth method, of class Paper.
     */
    @Test
    public void testGetWidth() {
        System.out.println("getWidth");
        Paper instance = Paper.A4;
        double expResult = 210.0;
        double result = instance.getWidth();
        assertEquals(expResult, result, 0.1);
        
    }

    /**
     * Test of equals method, of class Paper.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object expected = new Paper(210, 297);
        Paper result = Paper.A4;
        assertEquals(expected, result);
        
    }

    /**
     * Test of hashCode method, of class Paper.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Paper instance = Paper.A4;
        int expResult = new Paper(210, 297).hashCode();
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Paper.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Paper instance = Paper.A4;
        String expResult = "210mm x 297mm";
        String result = instance.toString();
        assertEquals(expResult, result);
        
    }

    /**
     * Test of fromString method, of class Paper.
     */
    @Test
    public void testFromString() {
        System.out.println("fromString");
        String value = "210mm x 297mm";
        Paper expResult = Paper.fromString(value);
        Paper result = Paper.A4;
        assertEquals(expResult, result);
        
    }
}
