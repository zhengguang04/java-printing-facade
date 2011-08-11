package dk.apaq.printing.core;

/**
 *
 * @author michaelzachariassenkrog
 */
public class Justification {

    public static double getJustifedWidth(Paper paper, Margin margin) {
        return paper.getWidth() - margin.getLeft() - margin.getRight();
    }

    public static double getJustifiedHeight(Paper paper, Margin margin) {
        return paper.getHeight() - margin.getTop() - margin.getBottom();
    }
}
