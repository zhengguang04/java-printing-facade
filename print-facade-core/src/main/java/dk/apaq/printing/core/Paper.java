package dk.apaq.printing.core;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public class Paper {

    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getNumberInstance();

    public static final Paper A0 = new Paper(841, 1189);
    public static final Paper A1 = new Paper(594, 841);
    public static final Paper A2 = new Paper(420, 594);
    public static final Paper A3 = new Paper(297, 420);
    public static final Paper A4 = new Paper(210, 297);
    public static final Paper A5 = new Paper(148, 210);
    public static final Paper A6 = new Paper(105, 148);
    public static final Paper A7 = new Paper(74, 105);
    public static final Paper A8 = new Paper(52, 74);
    public static final Paper A9 = new Paper(37, 52);
    public static final Paper A10 = new Paper(26, 37);
    
    
    private final double width;
    private final double height;
    private final String displayString;

    public Paper(double width, double height) {
        this.width = width;
        this.height = height;

        this.displayString = NUMBER_FORMAT.format(width) + "mm x " + NUMBER_FORMAT.format(height) + "mm";
    }

    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Paper other = (Paper) obj;
        if (Double.doubleToLongBits(this.width) != Double.doubleToLongBits(other.width)) {
            return false;
        }
        if (Double.doubleToLongBits(this.height) != Double.doubleToLongBits(other.height)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.width) ^ (Double.doubleToLongBits(this.width) >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this.height) ^ (Double.doubleToLongBits(this.height) >>> 32));
        return hash;
    }

    @Override
    public String toString() {
        return displayString;
    }

    public static Paper fromString(String value) {
        String[] array = value.split("x");
        if (array.length != 2) {
            throw new IllegalArgumentException("Value should follow syntax '<double>mm x <double>mm");
        }

        try {
            double width = NUMBER_FORMAT.parse(array[0].trim().replace("mm", "")).doubleValue();
            double height = NUMBER_FORMAT.parse(array[1].trim().replace("mm", "")).doubleValue();
            return new Paper(width, height);
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Value should follow syntax '<double>mm x <double>mm");
        }

    }
}
