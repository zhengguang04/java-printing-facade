/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.printing.core;

import junit.framework.TestCase;

/**
 *
 * @author michaelzachariassenkrog
 */
public class JustificationTest extends TestCase {
    
    public JustificationTest(String testName) {
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
     * Test of getJustifedWidth method, of class Justification.
     */
    public void testGetJustifedWidth() {
        System.out.println("getJustifedWidth");
        Paper paper = Paper.A4;
        Margin margin = new Margin(10, 10, 10, 10);
        double expResult = 190;
        double result = Justification.getJustifedWidth(paper, margin);
        assertEquals(expResult, result, 0.0);
        
    }

    /**
     * Test of getJustifiedHeight method, of class Justification.
     */
    public void testGetJustifiedHeight() {
        System.out.println("getJustifiedHeight");
        Paper paper = Paper.A4;
        Margin margin = new Margin(10, 10, 10, 10);
        double expResult = 277;
        double result = Justification.getJustifiedHeight(paper, margin);
        assertEquals(expResult, result, 0.0);
    }

}
