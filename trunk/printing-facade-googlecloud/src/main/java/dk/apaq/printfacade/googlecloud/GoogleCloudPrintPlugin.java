package dk.apaq.printfacade.googlecloud;

import dk.apaq.printing.core.AbstractPrinterManagerPlugin;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterJob;
import java.util.List;

/**
 *
 * @author krog
 */
public class GoogleCloudPrintPlugin extends AbstractPrinterManagerPlugin {

    /*
     * http://code.google.com/p/google-api-java-client/wiki/OAuth2Draft10#Sample_Program
     * http://code.google.com/apis/cloudprint/docs/appInterfaces.html
     * http://code.google.com/apis/accounts/docs/OAuth2.html#SS
     */
    private final String authorizationCode;

    public GoogleCloudPrintPlugin(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }
    
    public Printer getDefaultPrinter() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<Printer> getPrinters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void print(PrinterJob job) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    private String getAccessToken() {
        return null;
    }
}
