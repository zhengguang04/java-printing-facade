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
public class MarginTest extends TestCase {
    
    public MarginTest(String testName) {
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
     * Test of getBottom method, of class Margin.
     */
    public void testGet() {
        System.out.println("get");
        Margin instance = new Margin(10, 20, 30, 40);
        assertEquals(10.0, instance.getLeft());
        assertEquals(20.0, instance.getTop());
        assertEquals(30.0, instance.getRight());
        assertEquals(40.0, instance.getBottom());

    }


}
