package dk.apaq.printfacade.remoteclient;

/**
 *
 * @author michaelzachariassenkrog
 */
public interface RemoteClientPluginTransporter {

    public void transportPrinterJob(byte[] zipdata);
}
