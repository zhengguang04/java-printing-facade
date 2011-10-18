package dk.apaq.printfacade.googlecloud;

import com.google.gson.Gson;
import dk.apaq.printing.core.AbstractPrinterManagerPlugin;
import dk.apaq.printing.core.Margin;
import dk.apaq.printing.core.Paper;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterException;
import dk.apaq.printing.core.PrinterJob;
import dk.apaq.printing.core.PrinterState;
import dk.apaq.printing.core.util.PdfUtil;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author krog
 */
public class GoogleCloudPrintPlugin extends AbstractPrinterManagerPlugin {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleCloudPrintPlugin.class);
    /*
     * http://code.google.com/p/google-api-java-client/wiki/OAuth2Draft10#Sample_Program
     * http://code.google.com/apis/cloudprint/docs/appInterfaces.html
     * http://code.google.com/apis/accounts/docs/OAuth2.html#SS
     * 
     * code = 4/s0xkDRwfdiZR9rthiZdDCqKvekjP
     * clientid = 700939733854.apps.googleusercontent.com
     * clientSecret = yygJ1QE3yRsNieDoZKOKBUgl
     */
    private final Gson gson = new Gson();
    private final Base64 base64 = new Base64(0);
    private final Authorizer authorizer;
    private final String clientName;
    private List<Printer> printers = null;
    private String authCode;

    public interface Authorizer {

        void authorize(AuthorizeCallback callback);
    }
    
    public interface AuthorizeCallback {
    
        public void onAuthorized(String authorizationCode);
    }
    
    private class AuthorizeCallImpl implements AuthorizeCallback {
        private GoogleCloudPrintPlugin instance;

        public AuthorizeCallImpl(GoogleCloudPrintPlugin instance) {
            this.instance = instance;
        }

        public void onAuthorized(String authorizationCode) {
            instance.setAuthorizationCode(authorizationCode);
        }
        
        
    }
    
    public class CloudPrintJobStatus {

        private boolean success;
        private String message;

        public CloudPrintJobStatus(boolean success) {
            this.success = success;
            
        }

        public CloudPrintJobStatus(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
        
        
    }


    public class CloudPrinters {

        private boolean success;
        private List<CloudPrinter> printers;

        public CloudPrinters(boolean success) {
            this.success = success;
            this.printers = new ArrayList<CloudPrinter>();
        }

        public CloudPrinters(boolean success, List<CloudPrinter> printers) {
            this.success = success;
            this.printers = printers;
        }

        public List<CloudPrinter> getPrinters() {
            return printers;
        }

        public void setPrinters(List<CloudPrinter> printers) {
            this.printers = printers;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }
    }

    public class CloudPrinter implements Printer {

        private String id;
        private String name;
        private String description;
        private String proxy;
        private String status;
        private String capsHash;
        private String createTime;
        private String updateTime;
        private String accessTime;
        private boolean confirmed;
        private int numberOfDocuments;
        private int numberOfPages;

        public String getAccessTime() {
            return accessTime;
        }

        public void setAccessTime(String accessTime) {
            this.accessTime = accessTime;
        }

        public String getCapsHash() {
            return capsHash;
        }

        public void setCapsHash(String capsHash) {
            this.capsHash = capsHash;
        }

        public boolean isConfirmed() {
            return confirmed;
        }

        public void setConfirmed(boolean confirmed) {
            this.confirmed = confirmed;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getNumberOfDocuments() {
            return numberOfDocuments;
        }

        public void setNumberOfDocuments(int numberOfDocuments) {
            this.numberOfDocuments = numberOfDocuments;
        }

        public int getNumberOfPages() {
            return numberOfPages;
        }

        public void setNumberOfPages(int numberOfPages) {
            this.numberOfPages = numberOfPages;
        }

        public String getProxy() {
            return proxy;
        }

        public void setProxy(String proxy) {
            this.proxy = proxy;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public boolean supportsColor() {
            return true;
        }

        public Paper[] getSupportedPapers() {
            return new Paper[]{Paper.A4};
        }

        public Margin getPhysicalMargin(Paper paperSize) {
            return new Margin(0, 0, 0, 0);
        }

        public PrinterState getState() {
            return PrinterState.Idle;
        }
    }

    public GoogleCloudPrintPlugin(Authorizer authorizer, String clientName) {
        this.authorizer = authorizer;
        this.clientName = clientName;
    }

    public Printer getDefaultPrinter() {
        return null;
    }

    public List<Printer> getPrinters() {
        if(printers==null) {
            printers = new ArrayList<Printer>();
            authorizer.authorize(new AuthorizeCallImpl(this));
        }
        return printers;
    }

    public void print(PrinterJob job) {
        try {
            String charset = "UTF-8";
                if(authCode == null) {
                    throw new PrinterException("authcode was null");
                }
                URL url = new URL("https://www.google.com/cloudprint/submit?output=json");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");

                byte[] docData = PdfUtil.buildPdf(job);
                OutputStream fos = new FileOutputStream("test.pdf");
                fos.write(docData);
                fos.flush();
                fos.close();
                
                String queryString =
                        "printerid=" + URLEncoder.encode(job.getPrinter().getId(), charset)
                        + "&capabilities=" + URLEncoder.encode("", charset)
                        + "&contentType=" + URLEncoder.encode("application/pdf", charset)
                        + "&title=" + URLEncoder.encode(job.getName(), charset)
                        + "&content=" + URLEncoder.encode(base64.encodeToString(docData), charset);

                byte[] data = queryString.getBytes(charset);
                
                con.setRequestProperty("X-CloudPrint-Proxy", this.clientName);
                con.setRequestProperty("Authorization", "GoogleLogin auth=" + this.authCode);

                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.setRequestProperty("Accept-Charset", charset);
                con.addRequestProperty("Content-Length", Integer.toString(data.length));

                OutputStream os = con.getOutputStream();
                os.write(data);
                os.flush();
                os.close();
                
                // Get response
                CloudPrintJobStatus printJobStatus = gson.fromJson(new InputStreamReader(con.getInputStream()), CloudPrintJobStatus.class);

                if(!printJobStatus.isSuccess()) {
                    throw new PrinterException(printJobStatus.getMessage());
                }
            } catch (Exception ex) {
                throw new PrinterException(ex);
            }
    }

    public void setAuthorizationCode(String authCode) {
        this.authCode = authCode;
        this.retrieveCloudPrinters();
    }
    
    private void retrieveCloudPrinters() {

        if(authCode==null) {
            throw new PrinterException("authcode was null.");
        }

        try {
            URL url = new URL("https://www.google.com/cloudprint/search?output=json");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.addRequestProperty("X-CloudPrint-Proxy", clientName);
            con.addRequestProperty("Authorization", "GoogleLogin auth=" + authCode);

            CloudPrinters cplist = gson.fromJson(new InputStreamReader(con.getInputStream()), CloudPrinters.class);


            printers = new ArrayList<Printer>();
            printers.addAll(cplist.getPrinters());
            fireChangeEvent();
        } catch (Exception ex) {
            throw new PrinterException(ex);
        }

    }
    
}
