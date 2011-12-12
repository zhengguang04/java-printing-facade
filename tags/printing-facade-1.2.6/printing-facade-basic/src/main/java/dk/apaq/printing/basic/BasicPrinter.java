package dk.apaq.printing.basic;

import dk.apaq.printing.core.Margin;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterJob.DataType;
import dk.apaq.printing.core.PrinterState;
import java.util.HashMap;
import java.util.Map;
import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
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

    private final PrintService ps;
    private final Map<Paper, Margin> map = new HashMap<Paper, Margin>();


    public BasicPrinter(PrintService ps) {
        this.ps = ps;
        this.createMediaSizeArray(ps);
    }

    public String getId() {
        StringBuilder sb = new StringBuilder();
        sb.append(BasicPlugin.class.getSimpleName()).
                //append("#").
                //append(hashCode()).
                append("_").
                append(getName().replace(" ", "_"));
        return sb.toString();
    }

    public String getName() {
        return ps.getName();
    }

    public String getDescription() {
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
    
    public PrintService getAwtPrintService() {
        return ps;
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
                            
                            double left = Math.max(values[0], 0);
                            double top = Math.max(values[1], 0);
                            double right = Math.max(width - values[2] - values[0], 0);
                            double bottom = Math.max(height - values[3] - values[1], 0);
                            Margin margin = new Margin(left, top, right, bottom);

                            map.put(paper, margin);
                            break;
                        }
                }

            }
        }
        
    }

    @Override
    public boolean supportDatatype(DataType dataType) {
        switch(dataType) {
            case Pageable:
                return true;
            case Postscript:
                for(DocFlavor df : ps.getSupportedDocFlavors()) {
                    if(df.getMimeType().equalsIgnoreCase(DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType())) {
                        return true;
                    }
                }
        }
        return false;
    }
    
    
    

}
