/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package dk.apaq.printing.basic;

import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.Printer;

/**
 *
 * @author michael
 */
public class Main {

    
    public static void main(String[] args) {
        BasicPlugin basicPlugin = new BasicPlugin();

        for(Printer printer : basicPlugin.getPrinters()) {
            System.out.println(printer.getName() + "***************");
            for(Paper paper : printer.getSupportedPapers()) {
                System.out.println("Paper: " + paper);
                System.out.println("Margin: " + printer.getPhysicalMargin(paper));
            }
        }
    }
}
