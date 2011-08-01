package dk.apaq.printing.core;

import java.text.NumberFormat;

/**
 *
 * @author michael
 */
public class Margin {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance();

    private final double left;
    private final double top;
    private final double right;
    private final double bottom;
    private final String displayString;

    /**
     * Margin calculated as distance from the borders of a page in millimeters.
     */
    public Margin(double left, double top, double right, double bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;

        StringBuilder builder = new StringBuilder();
        builder.append("Left: ").append(NUMBER_FORMAT.format(left)).append("mm, ");
        builder.append("Top: ").append(NUMBER_FORMAT.format(top)).append("mm, ");
        builder.append("Right: ").append(NUMBER_FORMAT.format(right)).append("mm, ");
        builder.append("Bottom: ").append(NUMBER_FORMAT.format(bottom)).append("mm");
        displayString = builder.toString();
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

    @Override
    public String toString() {
        return displayString;
    }


}
