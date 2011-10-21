package dk.apaq.printing.googlecloud;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author krog
 */
public class TestPrint {

    public static void main(String[] args) throws IOException {
        TestPrint testPrint = new TestPrint();
        testPrint.TestGetPrinters();
    }
    
    public void TestGetPrinters() throws IOException {
        GoogleCloudPrint cloudPrint = new GoogleCloudPrint();
        cloudPrint.setUsername("user@gmail.com");
        cloudPrint.setPassword("password");
        CloudPrinters printers = cloudPrint.getPrinters();
    }

    public void TestSendPrintJob() {
        GoogleCloudPrint cloudPrint = new GoogleCloudPrint();
        cloudPrint.setUsername("username@gmail.com");
        cloudPrint.setPassword("password");
        cloudPrint.PrintDocument("1234", "Invoice", null/*BlobHelper.LoadFromFile(@"c:\75249.pdf")*/);
    }

    public class GoogleCloudPrint {

        private String username;
        private String password;
        private String source;

        public GoogleCloudPrint() {
            source = "basewebtek-youreontime-v1";
        }

        public String getUserName() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getSource() {
            return source;
        }

        public CloudPrintJob PrintDocument(String printerId, String title, byte[] document) {
            try {
                String authCode = authorize();
                if(authCode == null) {
                    return new CloudPrintJob(false);
                }
                URL url = new URL("http://www.google.com/cloudprint/submit?output=json");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setDoOutput(true);
                con.setRequestMethod("POST");

                String queryString =
                        "printerid=" + URLEncoder.encode(printerId)
                        + "&capabilities=" + URLEncoder.encode("")
                        + "&contentType=" + URLEncoder.encode("application/pdf")
                        + "&title=" + URLEncoder.encode(title)
                        + "&content=" + URLEncoder.encode(null/*Convert.ToBase64String(document)*/);

                byte[] data = null; //new ASCIIEncoding().GetBytes(queryString);

                con.addRequestProperty("X-CloudPrint-Proxy", source);
                con.addRequestProperty("Authorization", "GoogleLogin auth=" + authCode);

                con.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                con.addRequestProperty("Content-Length", Integer.toString(data.length));

                /*Stream stream = request.GetRequestStream();
                stream.Write(data, 0, data.Length);
                stream.Close();*/

                // Get response
                Gson gson = new Gson();
                CloudPrintJob printJob = gson.fromJson(new InputStreamReader(con.getInputStream()), CloudPrintJob.class);

                return printJob;
            } catch (Exception ex) {
                return new CloudPrintJob(false, ex.getMessage());
            }
        }

        public CloudPrinters getPrinters() throws IOException {

            
            String authCode = authorize();
            if(authCode==null) {
                return new CloudPrinters(false);
            }
            
            try {
                URL url = new URL("https://www.google.com/cloudprint/search?output=json");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                //con.setDoOutput(true);
                //con.setRequestMethod("POST");

                con.addRequestProperty("X-CloudPrint-Proxy", source);
                con.addRequestProperty("Authorization", "GoogleLogin auth=" + authCode);

                //con.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                //con.addRequestProperty("Content-Length", Integer.toString(1));

                String response = readAll(con.getInputStream());
                Gson gson = new Gson();
                CloudPrinters printers = gson.fromJson(new StringReader(response), CloudPrinters.class);


                return printers;
            } catch (Exception ex) {
                return null;
            }

        }

        private String authorize() throws IOException {
            boolean result = false;
            String authCode = "";

            URL url = new URL("https://www.google.com/accounts/ClientLogin?accountType=HOSTED_OR_GOOGLE&Email=" + this.username + "&Passwd=" + this.password + "&service=cloudprint&source=" + this.source);

            String responseContent = readAll(url.openStream());

            String[] split = responseContent.split("\n");
            for (String s : split) {
                String[] nvsplit = s.split("=");
                if (nvsplit.length == 2) {
                    if (nvsplit[0].equals("Auth")) {
                        authCode = nvsplit[1];
                        return authCode;
                    }
                }
            }

            return null;
        }
    }

    public class CloudPrintJob {

        private boolean success;
        private String message;

        public CloudPrintJob(boolean success) {
            this.success = success;
            
        }

        public CloudPrintJob(boolean success, String message) {
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

    public class CloudPrinter {

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
        
    }
    
    private String readAll(InputStream in) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        
        String line;
        while((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\r\n");
        }
        br.close();
        
        return sb.toString();
    }
}
