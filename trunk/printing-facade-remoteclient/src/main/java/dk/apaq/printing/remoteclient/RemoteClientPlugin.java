package dk.apaq.printing.remoteclient;

import dk.apaq.printing.core.AbstractPrinterManagerPlugin;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterException;
import dk.apaq.printing.core.PrinterJob;
import dk.apaq.printing.core.util.ImageUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author michaelzachariassenkrog
 */
public class RemoteClientPlugin extends AbstractPrinterManagerPlugin {

    private final RemoteClientPluginTransporter transporter;
    private final List<Printer> printers = new ArrayList<Printer>();
    private String defaultId;
    private static final int RESOLUTION = 300;

    public RemoteClientPlugin(RemoteClientPluginTransporter transporter) {
        this.transporter = transporter;
    }

    @Override
    public Printer getDefaultPrinter() {
        if (defaultId == null) {
            return null;
        }

        for (Printer p : printers) {
            if (p.getId().equals(defaultId)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Printer> getPrinters() {
        return Collections.unmodifiableList(printers);
    }

    @Override
    public void print(PrinterJob job) {
        transporter.transportPrinterJob(buildZip(job));
    }

    public void setPrinters(List<RemoteClientPrinter> clientPrinters) {
        this.printers.clear();
        this.printers.addAll(clientPrinters);
        fireChangeEvent();
    }

    public void setDefaultPrinterId(String id) {
        this.defaultId = id;
    }

    private byte[] buildZip(PrinterJob job) {
        try {
            return ImageUtil.buildImage(job, RESOLUTION);
        } catch (IOException ex) {
            throw new PrinterException("Unable to print.", ex);
        }

    }
    
}
