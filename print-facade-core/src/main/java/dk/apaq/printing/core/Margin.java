package dk.apaq.printing.core;

/**
 *
 * @author michael
 */
public class Margin {

    private final double left;
    private final double top;
    private final double right;
    private final double bottom;

    /**
     * Margin calculated as distance from the borders of a page in millimeters.
     */
    public Margin(double left, double top, double right, double bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    /**
     * Printable area distace from the bottom of the paper in millimeters
     */
    public double getBottom() {
        return bottom;
    }

    /**
     * Printable area distace from the left of the paper in millimeters
     */
    public double getLeft() {
        return left;
    }

    /**
     * Printable area distace from the right of the paper in millimeters
     */
    public double getRight() {
        return right;
    }

    /**
     * Printable area distace from the top of the paper in millimeters
     */
    public double getTop() {
        return top;
    }
    
}
