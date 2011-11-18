package dk.apaq.printing.remoteclient;

import dk.apaq.printing.core.Margin;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterState;
import java.util.Map;

/**
 *
 * @author michaelzachariassenkrog
 */
public class RemoteClientPrinter implements Printer {

    private final String id;
    private final String name;
    private final String description;
    private final boolean colorSupported;
    private final Map<Paper, Margin> paperMap;
    private final PrinterState state;

    public RemoteClientPrinter(String id, String name, String description, boolean colorSupported, Map<Paper, Margin> paperMap, PrinterState state) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.colorSupported = colorSupported;
        this.paperMap = paperMap;
        this.state = state;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean supportsColor() {
        return colorSupported;
    }

    @Override
    public Paper[] getSupportedPapers() {
        return paperMap.keySet().toArray(new Paper[0]);
    }

    @Override
    public Margin getPhysicalMargin(Paper paperSize) {
        return paperMap.get(paperSize);
    }

    @Override
    public PrinterState getState() {
        return state;
    }

}
