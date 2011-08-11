package dk.apaq.printing.core;

/**
 *
 * @author michaelzachariassenkrog
 */
public interface PrinterListChangeNotifier {

    void addListener(PrinterListChangeListener listener);
    void removeListener(PrinterListChangeListener listener);
}
