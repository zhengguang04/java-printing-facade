package dk.apaq.printing.core;

/**
 *
 * @author michael
 */
public class Paper {

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
    
    public Paper(double width, double height) {
        this.width = width;
        this.height = height;
    }

    
    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    
}
