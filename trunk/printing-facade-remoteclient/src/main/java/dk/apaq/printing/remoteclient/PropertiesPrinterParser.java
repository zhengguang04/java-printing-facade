package dk.apaq.printing.remoteclient;

import dk.apaq.printing.core.Margin;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterState;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Read printers from a properties file. The expected keys are explained here.
 *
 * Defines number of printers in properties:
 * printers.count=<count, fx. 5>
 *
 * Definition for every printer:
 * printers.<index>.id=<id>
 * printers.<index>.name=<id>
 * printers.<index>.description=<id>
 * printers.<index>.colorSupported=<id>
 * printers.<index>.state=<id>
 * printers.<index>.papers.count=<count>
 * printers.<index>.papers.<index>.size=<size>
 * printers.<index>.papers.<index>.margin=<margin>
 *
 * Example
 * printers.count=1
 * printers.0.id=Samsung_printer
 * printers.0.name=Samsun_printer
 * printers.0.description=
 * printers.0.colorSupported=true
 * printers.0.state=Idle
 * printers.0.papers.count=1
 * printers.0.papers.0.size=210mm x 297mm
 * printers.0.papers.0.margin=5mm x 5mm x 5mm x 5mm
 *
 * @author michaelzachariassenkrog
 */
public class PropertiesPrinterParser {

    private static final String KEY_PRINTER_PREFIX = "printers.";
    private static final String KEY_PRINTER_COUNT = KEY_PRINTER_PREFIX + "count";
    private final Properties properties;


    public PropertiesPrinterParser(Properties properties) {
        this.properties = properties;
    }

    public int getPrinterCount() {
        return Integer.parseInt(properties.getProperty(KEY_PRINTER_COUNT));
    }

    public Printer getPrinter(int index) {
        String currentPrinterPrefix = KEY_PRINTER_PREFIX + index + ".";
        String id = properties.getProperty(currentPrinterPrefix + "id");
        String name = properties.getProperty(currentPrinterPrefix + "name");
        String description = properties.getProperty(currentPrinterPrefix + "description");
        boolean colorSupported = "true".equalsIgnoreCase(properties.getProperty(currentPrinterPrefix + "colorSupported"));
        
        PrinterState state;
        try{
            state = PrinterState.valueOf(properties.getProperty(currentPrinterPrefix + "state"));
        } catch(Exception ex) {
            state = PrinterState.Idle;
        }
        
        Map<Paper, Margin> paperMap = new HashMap<Paper, Margin>();
        
        int paperCount = Integer.parseInt(properties.getProperty(currentPrinterPrefix + "papers.count"));
        for(int i=0;i<paperCount;i++) {
            String paperString = properties.getProperty(currentPrinterPrefix + "papers." + i + ".size");
            String marginString = properties.getProperty(currentPrinterPrefix + "papers." + i + ".margin");
            Paper paper = Paper.fromString(paperString);
            Margin margin = Margin.fromString(marginString);
            
            paperMap.put(paper, margin);
        }
        
        return new RemoteClientPrinter(id, name, description, colorSupported, paperMap, state);
    }

}
