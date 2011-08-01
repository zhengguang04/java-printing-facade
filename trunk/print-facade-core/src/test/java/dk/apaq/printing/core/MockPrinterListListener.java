package dk.apaq.printing.core;

/**
 *
 * @author michael
 */
public class MockPrinterListListener implements PrinterListChangeListener {

    private boolean called;

    public void onPrinterListChange(PrinterEvent event) {
        called=true;
    }

    public boolean isCalled() {
        return called;
    }

    public void clear() {
        called=false;
    }

}
