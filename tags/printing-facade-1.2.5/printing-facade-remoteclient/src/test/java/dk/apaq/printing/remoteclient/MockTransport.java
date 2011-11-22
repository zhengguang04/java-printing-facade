package dk.apaq.printing.remoteclient;

import dk.apaq.printing.remoteclient.RemoteClientPluginTransporter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author michael
 */
public class MockTransport implements RemoteClientPluginTransporter {

    @Override
    public void transportPrinterJob(byte[] zipdata) {
        FileOutputStream os = null;
        try {
            File file = new File("printjob.zip");
            os = new FileOutputStream(file);
            os.write(zipdata);
            os.flush();
            os.close();
            
        } catch (IOException ex) {
            Logger.getLogger(MockTransport.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                os.close();
            } catch (IOException ex) {}
        }
    }
    
}
