package dk.apaq.printing.core.util;

import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.PrinterException;
import dk.apaq.printing.core.PrinterJob;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;

/**
 *
 * @author krog
 */
public class ImageUtil {
    
    /**
     * Renders the printjob into a zip file containing an image for each page. The zip also contains a file
     * called jo.properties which holds information about the printjob.
     */
    public static void buildImage(PrinterJob job, int dpi, OutputStream os) throws IOException {
            float scaleTo72Dpi = (float) dpi / 72;
            float mmTo72DpiScale = 2.835F;

            ZipOutputStream zout = new ZipOutputStream(os);

            ZipEntry jobinfoentry = new ZipEntry("job.properties");
            zout.putNextEntry(jobinfoentry);
            writeJobInfo(job, zout);
            zout.closeEntry();

            Paper paper = job.getPaper();
            int pixelWidth = (int) (paper.getWidth() * mmTo72DpiScale * scaleTo72Dpi);
            int pixelHeight = (int) (paper.getHeight() * mmTo72DpiScale * scaleTo72Dpi);


            try {
                for (int i = 0; i == 0 || i < job.getNumberOfPages(); i++) {
                    BufferedImage image = new BufferedImage(pixelWidth, pixelHeight, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D gfx = image.createGraphics();
                    gfx.scale(scaleTo72Dpi, scaleTo72Dpi);

                    job.render(gfx, i);
                    ZipEntry entry = new ZipEntry(i + ".png");
                    zout.putNextEntry(entry);
                    entry.setExtra("image/png".getBytes("utf-8"));
                    ImageIO.write(image, "png", zout);
                    zout.flush();
                    zout.closeEntry();
                    image.flush();
                }

            } catch (Exception ex) {
                throw new PrinterException(ex);

            } finally {
                zout.flush();
                os.flush();
                zout.close();
            }
    }
    
    public static byte[] buildImage(PrinterJob job, int dpi) throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageUtil.buildImage(job, dpi, os);
        return os.toByteArray();
    }
    
    private static void writeJobInfo(PrinterJob printerJob, OutputStream os) throws IOException {

        Writer w = new OutputStreamWriter(os);
        Properties p = new Properties();
        p.setProperty("printerId", printerJob.getPrinter().getId());
        p.setProperty("paper", printerJob.getPaper().toString());
        p.setProperty("margin", printerJob.getMargin().toString());
        p.setProperty("copies", Integer.toString(printerJob.getCopies()));
        p.store(w, null);

    }
}
