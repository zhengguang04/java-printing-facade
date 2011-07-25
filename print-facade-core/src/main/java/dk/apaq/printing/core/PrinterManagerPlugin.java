/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.printing.core;

import java.util.List;

/**
 *
 * @author michael
 */
public interface PrinterManagerPlugin {

    public List<Printer> getPrinters();
    public void print(PrinterJob job);
}
