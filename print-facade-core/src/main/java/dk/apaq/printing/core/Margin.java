package dk.apaq.printing.core;

import java.text.NumberFormat;
import java.text.ParseException;

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
        builder.append(NUMBER_FORMAT.format(left)).append("mm, ");
        builder.append(NUMBER_FORMAT.format(top)).append("mm, ");
        builder.append(NUMBER_FORMAT.format(right)).append("mm, ");
        builder.append(NUMBER_FORMAT.format(bottom)).append("mm");
        displayString = builder.toString();
    }

    /**
     * Printable area distance from the bottom of the paper in millimeters
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

    public static Margin fromString(String value) {
        String[] array = value.split(",");
        if (array.length != 4) {
            throw new IllegalArgumentException("Value should follow syntax '<double>mm, <double>mm, <double>mm, <double>mm");
        }

        try {
            double left = NUMBER_FORMAT.parse(array[0].trim().replace("mm", "")).doubleValue();
            double top = NUMBER_FORMAT.parse(array[1].trim().replace("mm", "")).doubleValue();
            double right = NUMBER_FORMAT.parse(array[2].trim().replace("mm", "")).doubleValue();
            double bottom = NUMBER_FORMAT.parse(array[3].trim().replace("mm", "")).doubleValue();
            return new Margin(left, top ,right, bottom);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Value should follow syntax '<double>mm, <double>mm, <double>mm, <double>mm");
        }

    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Margin other = (Margin) obj;
        if (Double.doubleToLongBits(this.left) != Double.doubleToLongBits(other.left)) {
            return false;
        }
        if (Double.doubleToLongBits(this.top) != Double.doubleToLongBits(other.top)) {
            return false;
        }
        if (Double.doubleToLongBits(this.right) != Double.doubleToLongBits(other.right)) {
            return false;
        }
        if (Double.doubleToLongBits(this.bottom) != Double.doubleToLongBits(other.bottom)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.left) ^ (Double.doubleToLongBits(this.left) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.top) ^ (Double.doubleToLongBits(this.top) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.right) ^ (Double.doubleToLongBits(this.right) >>> 32));
        hash = 67 * hash + (int) (Double.doubleToLongBits(this.bottom) ^ (Double.doubleToLongBits(this.bottom) >>> 32));
        return hash;
    }

}
