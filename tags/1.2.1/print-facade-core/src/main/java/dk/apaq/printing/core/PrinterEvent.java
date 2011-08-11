package dk.apaq.printing.core;

/**
 *
 * @author michaelzachariassenkrog
 */
public class PrinterEvent {

    private final PrinterManagerPlugin sourcePlugin;

    public PrinterEvent(PrinterManagerPlugin sourcePlugin) {
        this.sourcePlugin = sourcePlugin;
    }

    public PrinterManagerPlugin getSourcePlugin() {
        return sourcePlugin;
    }

}

