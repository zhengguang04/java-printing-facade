/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.printing.basic;

import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterJob;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttribute;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Media;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;

/**
 *
 * @author michael
 */
public class Main {

    public static class TestPrintable implements Printable {

        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            return Printable.NO_SUCH_PAGE;
        }

    }

    public static void printDefaultPageFormat() throws PrinterException {

        PrintService service = PrintServiceLookup.lookupDefaultPrintService();
        PageFormat pf = new PageFormat();

        if (service == null) {
            return;
        }

        PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
        attributes.add(MediaSizeName.ISO_A4);

        Media media = (Media) attributes.get(Media.class);
        MediaPrintableArea mpa = null;//(MediaPrintableArea) attributes.get(MediaPrintableArea.class);
        OrientationRequested orientReq = null;// OrientationRequested) attributes.get(OrientationRequested.class);

        if (media == null && mpa == null && orientReq == null) {
            return;
        }
        java.awt.print.Paper paper = pf.getPaper();

        /* If there's a media but no media printable area, we can try
         * to retrieve the default value for mpa and use that.
         */
        if (mpa == null && media != null
                && service.isAttributeCategorySupported(MediaPrintableArea.class)) {
            Object mpaVals = service.getSupportedAttributeValues(MediaPrintableArea.class, null, attributes);
            if (mpaVals instanceof MediaPrintableArea[]
                    && ((MediaPrintableArea[]) mpaVals).length > 0) {
                mpa = ((MediaPrintableArea[]) mpaVals)[0];
            }
        }

        if (media != null
                && service.isAttributeValueSupported(media, null, attributes)) {
            if (media instanceof MediaSizeName) {
                MediaSizeName msn = (MediaSizeName) media;
                MediaSize msz = MediaSize.getMediaSizeForName(msn);
                if (msz != null) {
                    double inch = 72.0;
                    double paperWid = msz.getX(MediaSize.INCH) * inch;
                    double paperHgt = msz.getY(MediaSize.INCH) * inch;
                    paper.setSize(paperWid, paperHgt);
                    if (mpa == null) {
                        paper.setImageableArea(inch, inch,
                                paperWid - 2 * inch,
                                paperHgt - 2 * inch);
                    }
                }
            }
        }

        if (mpa != null
                && service.isAttributeValueSupported(mpa, null, attributes)) {
            float[] printableArea =
                    mpa.getPrintableArea(MediaPrintableArea.INCH);
            for (int i = 0; i < printableArea.length; i++) {
                printableArea[i] = printableArea[i] * 72.0f;
            }
            paper.setImageableArea(printableArea[0], printableArea[1],
                    printableArea[2], printableArea[3]);
        }

        if (orientReq != null
                && service.isAttributeValueSupported(orientReq, null, attributes)) {
            int orient;
            if (orientReq.equals(OrientationRequested.REVERSE_LANDSCAPE)) {
                orient = PageFormat.REVERSE_LANDSCAPE;
            } else if (orientReq.equals(OrientationRequested.LANDSCAPE)) {
                orient = PageFormat.LANDSCAPE;
            } else {
                orient = PageFormat.PORTRAIT;
            }
            pf.setOrientation(orient);
        }

        pf.setPaper(paper);
        //pf = validatePage(pf);

        java.awt.print.PrinterJob pj = java.awt.print.PrinterJob.getPrinterJob();
        pf = pj.getPageFormat(attributes);
        pj.setPrintable(new TestPrintable());
        pj.print(attributes);
    }

    public static void main(String[] args) throws PrinterException {

        printDefaultPageFormat();
        BasicPlugin basicPlugin = new BasicPlugin();

        for (Printer printer : basicPlugin.getPrinters()) {
            System.out.println(printer.getName() + "***************");
            for (Paper paper : printer.getSupportedPapers()) {
                System.out.println("Paper: " + paper);
                System.out.println("Margin: " + printer.getPhysicalMargin(paper));
            }
        }
    }
}
