package dk.apaq.printing.core.util;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.DefaultFontMapper;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.PrinterException;
import dk.apaq.printing.core.PrinterJob;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 *
 * @author krog
 */
public class PdfUtil {
    
    public static final double MM_TO_DPI_SCALE = 2.8350168350168350168350168350168;
    public static final Graphics2D DUMMYRENDERSPACE = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
                
    
    public static void buildPdf(PrinterJob job, OutputStream os) throws DocumentException {
        Document doc = new Document(getPageSizeFromPaper(job.getPaper()));
        PdfWriter pdfWriter = PdfWriter.getInstance(doc, os);
        doc.open();
        
        for (int i = 0; job.getNumberOfPages() < 0 || i < job.getNumberOfPages(); i++) {
            
            if(job.getNumberOfPages()<0) {
                if(!job.render(DUMMYRENDERSPACE, i)) {
                    break;
                }
            }
            
            PdfContentByte cb = pdfWriter.getDirectContent();
            PdfTemplate pdfTemplate = cb.createTemplate(doc.getPageSize().getWidth(), doc.getPageSize().getHeight());
            Graphics2D g2d = pdfTemplate.createGraphics(doc.getPageSize().getWidth(), doc.getPageSize().getHeight(), new DefaultFontMapper());
            try {
                if(job.render(g2d, i)) {
                    g2d.dispose();
                    if(i>0) {
                        doc.newPage();
                    }
                    cb.addTemplate(pdfTemplate,0 ,0);
                } else {
                    throw new PrinterException("Unknow error occured.");
                }
            } catch(PrinterException ex) {
                break;
            } finally {
                
            }
        }
        doc.close();
    }
    
    public static byte[] buildPdf(PrinterJob job) throws DocumentException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        buildPdf(job, os);
        return os.toByteArray();
    }
    
    private static Rectangle getPageSizeFromPaper(Paper paper) {
        return new Rectangle((float)(paper.getWidth() * MM_TO_DPI_SCALE), (float) (paper.getHeight() * MM_TO_DPI_SCALE));
    }
}
