package dk.apaq.printing.googlecloud;

import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterJob;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author krog
 */
public class TestPrint {

    public static void main(String[] args) throws IOException {
        OAuth2Authorizer instance = new OAuth2Authorizer("700939733854.apps.googleusercontent.com", "TwM1ADz1REuRfd5muU89Rejv", "4/F7MVfYWRE6ZESprdRF6iGlQuxG2k");
        GoogleCloudPrintPlugin cloudPrintPlugin = new GoogleCloudPrintPlugin(instance, "wullawulla");
        
        List<Printer> printers = cloudPrintPlugin.getPrinters();
        
        Printer cp = null;
        for(Printer printer : printers) {
            if(printer.getName().contains("Cute")) {
                cp = printer;
                break;
            }
        }
        
        PrinterJob job = PrinterJob.getBuilder(cp, new Printable() {

            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if(pageIndex>0) return NO_SUCH_PAGE;
                graphics.drawString("blabla", 100, 100);
                return PAGE_EXISTS;
            }
        }).build();
        cloudPrintPlugin.print(job);
    }
    
    
}
