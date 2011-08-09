package dk.apaq.printfacade.remoteclient;

import dk.apaq.printing.core.AbstractPrinterManagerPlugin;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterException;
import dk.apaq.printing.core.PrinterJob;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.imageio.ImageIO;
import javax.print.PrintException;
import javax.print.attribute.PrintRequestAttributeSet;

/**
 *
 * @author michaelzachariassenkrog
 */
public class RemoteClientPlugin extends AbstractPrinterManagerPlugin {

    private final RemoteClientPluginTransporter transporter;
    private final List<Printer> printers = new ArrayList<Printer>();
    private String defaultId;
    private static final int RESOLUTION = 300;

    public RemoteClientPlugin(RemoteClientPluginTransporter transporter) {
        this.transporter = transporter;
    }

    @Override
    public Printer getDefaultPrinter() {
        if (defaultId == null) {
            return null;
        }

        for (Printer p : printers) {
            if (p.getId().equals(defaultId)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Printer> getPrinters() {
        return Collections.unmodifiableList(printers);
    }

    @Override
    public void print(PrinterJob job) {
        //Build zip
        //Use custom transport to client
        //transporter.transportPrinterJob(zipdata);
    }

    public void setPrinters(List<RemoteClientPrinter> clientPrinters) {
        this.printers.clear();
        this.printers.addAll(printers);
        fireChangeEvent();
    }

    public void setDefaultPrinterId(String id) {
        this.defaultId = id;
    }

    private byte[] buildZip(PrinterJob job) {
        try {
            //String flavorClassName = doc.getDocFlavor().getRepresentationClassName();
            float scaleTo72Dpi = (float) RESOLUTION / 72;
            float mmTo72DpiScale = 2.835F;

            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ZipOutputStream zout = new ZipOutputStream(os);

            ZipEntry jobinfoentry = new ZipEntry("job.json");
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

            return os.toByteArray();

        } catch (IOException ex) {
            throw new PrinterException("Unable to print.", ex);
        }

    }

    private void writeJobInfo(PrinterJob printerJob, OutputStream os) throws IOException {

        Writer w = new OutputStreamWriter(os);
        Properties p = new Properties();
        p.setProperty("printerId", printerJob.getPrinter().getId());
        p.setProperty("paper", printerJob.getPaper().toString());
        p.setProperty("margin", printerJob.getMargin().toString());
        p.setProperty("copies", Integer.toString(printerJob.getCopies()));
        p.store(w, null);

    }
}
