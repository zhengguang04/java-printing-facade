package dk.apaq.printing.core;

/**
 *
 * @author michaelzachariassenkrog
 */
public class MockPrinter implements Printer {

    private final String id;
    private final String name;
    private final String description;
    private final boolean supportsColor;
    private final Paper[] papers;

    public MockPrinter(String id, String name, String description, boolean supportsColor, Paper[] papers) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.supportsColor = supportsColor;
        this.papers = papers;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String description() {
        return description;
    }

    public boolean supportsColor() {
        return supportsColor;
    }

    public Paper[] getSupportedPapers() {
        return papers;
    }

    public Margin getPhysicalMargin(Paper paperSize) {
        return new Margin(10, 10, 10, 10);
    }

    public PrinterState getState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
