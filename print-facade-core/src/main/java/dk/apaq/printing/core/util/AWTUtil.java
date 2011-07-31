package dk.apaq.printing.core.util;

import dk.apaq.printing.core.Justification;
import dk.apaq.printing.core.Margin;
import dk.apaq.printing.core.Orientation;
import dk.apaq.printing.core.Paper;
import java.awt.print.PageFormat;

/**
 *
 * @author michaelzachariassenkrog
 */
public class AWTUtil {

    public static PageFormat generatePageformat(Paper paper, Margin margin, Orientation orientation) {
        PageFormat format = new PageFormat();
        format.setPaper(getAwtPaper(paper, margin));
        format.setOrientation(getAwtOrientation(orientation));
        return format;
    }

    public static java.awt.print.Paper getAwtPaper(Paper paper, Margin margin) {
        java.awt.print.Paper awtPaper = new java.awt.print.Paper();
        awtPaper.setImageableArea(margin.getLeft(), margin.getTop(),
                Justification.getJustifedWidth(paper, margin),
                Justification.getJustifiedHeight(paper, margin));
        awtPaper.setSize(paper.getWidth(), paper.getHeight());
        return awtPaper;
    }

    public static int getAwtOrientation(Orientation orientation) {
        switch(orientation) {
            case Landscape:
                return PageFormat.LANDSCAPE;
            case Portrait:
                return PageFormat.PORTRAIT;
            case ReverseLandscape:
                return PageFormat.REVERSE_LANDSCAPE;
        }
        return -1;
    }
}
