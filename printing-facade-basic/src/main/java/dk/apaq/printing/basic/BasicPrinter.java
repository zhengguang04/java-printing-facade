package dk.apaq.printing.basic;

import dk.apaq.printing.core.Margin;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterState;
import java.util.HashMap;
import java.util.Map;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.ColorSupported;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterInfo;

/**
 *
 * @author michael
 */
public class BasicPrinter implements Printer {

    private static final Paper[] CHECKED_PAPER_LIST = {Paper.A0, Paper.A1, Paper.A2,
                                                        Paper.A3, Paper.A4, Paper.A5,
                                                        Paper.A6, Paper.A7, Paper.A8,
                                                        Paper.A9, Paper.A10};
    private final PrintService ps;
    private final Map<Paper, Margin> map = new HashMap<Paper, Margin>();


    public BasicPrinter(PrintService ps) {
        this.ps = ps;
        this.createMediaSizeArray(ps);
        /*for(Paper paper : CHECKED_PAPER_LIST) {
            checkAndAddSize(paper);
        }*/
    }

    public String getId() {
        return BasicPlugin.class.getSimpleName() + "#" + hashCode() + "_" + getName();
    }

    public String getName() {
        return ps.getName();
    }

    public String description() {
        PrinterInfo attr = ps.getAttribute(PrinterInfo.class);
        if(attr == null) {
            return null;
        } else {
            return attr.getValue();
        }
    }

    public boolean supportsColor() {
        ColorSupported attr = ps.getAttribute(ColorSupported.class);
        if(attr == null) {
            return false;
        } else {
            return attr.getValue() == ColorSupported.SUPPORTED.getValue();
        }
    }

    public Paper[] getSupportedPapers() {
        return map.keySet().toArray(new Paper[0]);
    }

    public Margin getPhysicalMargin(Paper paper) {
        return map.get(paper);
    }

    public PrinterState getState() {
        javax.print.attribute.standard.PrinterState attr = ps.getAttribute(javax.print.attribute.standard.PrinterState.class);
        if(attr == null) {
            return PrinterState.Idle;
        } else {
            return attr.getValue() == javax.print.attribute.standard.PrinterState.IDLE.getValue() ? PrinterState.Idle : PrinterState.Busy;
        }
    }


    private void createMediaSizeArray(PrintService ps) {
        Object o = ps.getSupportedAttributeValues(Media.class, null, null);
        if (o != null) {
            Media[] sizes = (Media[]) o;
            for (Media media : sizes) {
                if (!(media instanceof MediaSizeName)) {
                    continue;
                }
                MediaSizeName mediaSizeName = (MediaSizeName) media;
                MediaSize size = MediaSize.getMediaSizeForName(mediaSizeName);

                HashPrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
                attributeSet.add(mediaSizeName);
                o = ps.getSupportedAttributeValues(MediaPrintableArea.class, null, attributeSet);
                if(o==null) {
                    continue;
                }

                MediaPrintableArea[] areas = (MediaPrintableArea[]) o;
                if(areas.length==0) {
                    continue;
                }

                for (MediaPrintableArea area : areas) {
                        if (area != null) {
                            float[] values = area.getPrintableArea(MediaPrintableArea.MM);
                            double width = size.getX(MediaSize.MM);
                            double height = size.getY(MediaSize.MM);
                            Paper paper = new Paper(width, height);
                            Margin margin = new Margin(values[0], values[1], width - values[2] - values[0] , height - values[3] - values[1]);

                            map.put(paper, margin);
                            break;
                        }
                }

            }
        }
        
    }
    

}