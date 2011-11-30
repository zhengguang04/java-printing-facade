package dk.apaq.printing.core.util;

import com.sun.tools.javac.zip.ZipFileIndexEntry;
import dk.apaq.printing.core.Margin;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.PrinterException;
import dk.apaq.printing.core.PrinterJob;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.SimpleDoc;
import javax.print.StreamPrintService;
import javax.print.StreamPrintServiceFactory;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;

/**
 *
 * @author michael
 */
public class PsUtil {

    private static class PrinterJobPrintable implements Printable {

        private final PrinterJob job;

        public PrinterJobPrintable(PrinterJob job) {
            this.job = job;
        }

        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
            if(job.getNumberOfPages()>=0) {
                if (pageIndex >= job.getNumberOfPages()) {
                    return NO_SUCH_PAGE;
                }
            }

            if(job.render((Graphics2D) graphics, pageIndex)) {
                return PAGE_EXISTS;
            } else {
                return NO_SUCH_PAGE;
            }
        }
    }

    public static void buildPostscript(PrinterJob printerJob, OutputStream os) {
        // Find a factory that can do the conversion
        DocFlavor flavor = DocFlavor.SERVICE_FORMATTED.PRINTABLE;
        StreamPrintServiceFactory[] factories = StreamPrintServiceFactory.lookupStreamPrintServiceFactories(flavor,
                DocFlavor.BYTE_ARRAY.POSTSCRIPT.getMimeType());

        if (factories.length > 0) {
            try {
                StreamPrintService service = factories[0].getPrintService(os);
                PrinterJobPrintable printable = new PrinterJobPrintable(printerJob);
                Paper paper = printerJob.getPaper();
                Margin margin = printerJob.getMargin();

                PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                MediaSizeName mediaSizeName = MediaSize.findMedia(((Double) paper.getWidth()).floatValue(), ((Double) paper.getHeight()).floatValue(), MediaSize.MM);
                
                if(mediaSizeName==null) {
                    throw new PrinterException("No mediasize found for given paper [paper="+paper.toString()+"]");
                }
                aset.add(mediaSizeName);
                aset.add(AWTUtil.getPrintableArea(paper, margin));
                
                Doc doc = new SimpleDoc(printable, flavor, null);
                DocPrintJob job = service.createPrintJob();
                job.print(doc, aset);
                os.close();
        
            } catch (IOException ex) {
                throw new PrinterException(ex);
            }catch (PrintException ex) {
                throw new PrinterException(ex);
            }
        } else {
            throw new PrinterException("Streaming to postscript file is not supported on this platform.");
        }

    }
    
    public static byte[] buildPostscript(PrinterJob job, boolean zip) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        
        if(zip) {
            ZipOutputStream zos = new ZipOutputStream(bos);
            
            ZipEntry jobinfoentry = new ZipEntry("job.properties");
            zos.putNextEntry(jobinfoentry);
            JobInfoUtil.writeJobInfo(job, zos);
            
            ZipEntry entry = new ZipEntry("job.ps");
            zos.putNextEntry(entry);
            PsUtil.buildPostscript(job, zos);
            
            zos.close();
        } else {
            PsUtil.buildPostscript(job, bos);
        }
        return bos.toByteArray();
    }
}
