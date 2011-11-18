package dk.apaq.printing.remoteclient;

/**
 *
 * @author michaelzachariassenkrog
 */
public interface RemoteClientPluginTransporter {

    public void transportPrinterJob(byte[] zipdata);
}
