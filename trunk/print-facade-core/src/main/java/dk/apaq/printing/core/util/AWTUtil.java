package dk.apaq.printing.core.util;

import dk.apaq.printing.core.Justification;
import dk.apaq.printing.core.Margin;
import dk.apaq.printing.core.Orientation;
import dk.apaq.printing.core.Paper;
import java.awt.print.PageFormat;
import javax.print.attribute.standard.MediaPrintableArea;

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

    public static MediaPrintableArea getPrintableArea(Paper paper, Margin margin) {
        return new MediaPrintableArea(((Double)margin.getLeft()).floatValue(),
                ((Double)margin.getTop()).floatValue(),
                ((Double)Justification.getJustifedWidth(paper, margin)).floatValue(),
                ((Double)Justification.getJustifiedHeight(paper, margin)).floatValue(),
                MediaPrintableArea.MM);
    }

    public static java.awt.print.Paper getAwtPaper(Paper paper, Margin margin) {
        java.awt.print.Paper awtPaper = new java.awt.print.Paper();
        awtPaper.setImageableArea(mmToPixels(margin.getLeft()),
                mmToPixels(margin.getTop()),
                mmToPixels(Justification.getJustifedWidth(paper, margin)),
                mmToPixels(Justification.getJustifiedHeight(paper, margin)));
        awtPaper.setSize(mmToPixels(paper.getWidth()), mmToPixels(paper.getHeight()));
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

    private static int mmToPixels(double mm) {
        return (int) (mm / 10 / 2.54 * 72);
    }
}
