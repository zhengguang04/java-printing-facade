package dk.apaq.printing.googlecloud;

import dk.apaq.printing.core.PrinterException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

/**
 *
 * @author krog
 */
public class ClientLoginAuthorizer implements GoogleCloudPrintPlugin.Authorizer {

    private final String source;
    private final CredentialsSupplier credentialsSupplier;

    public interface CredentialsSupplier {
        void retrieveCredentials(CredentialsCallback callback);
    }
    
    public interface CredentialsCallback {
        void onCredentialsRetrieved(String username, String password);
    }
    
    private static class SimpleSupplier implements CredentialsSupplier {
        private final String username;
        private final String password;

        public SimpleSupplier(String username, String password) {
            this.username = username;
            this.password = password;
        }

        public void retrieveCredentials(CredentialsCallback callback) {
            callback.onCredentialsRetrieved(username, password);
        }
        
    }
    
    public ClientLoginAuthorizer(CredentialsSupplier credentialsSupplier, String source) {
        this.credentialsSupplier = credentialsSupplier;
        this.source = source;
    }
    
    public ClientLoginAuthorizer(String username, String password, String source) {
        this(new SimpleSupplier(username, password), source);
    }
    
    public void authorize(final GoogleCloudPrintPlugin.AuthorizeCallback callback) {
        credentialsSupplier.retrieveCredentials(new CredentialsCallback() {

            public void onCredentialsRetrieved(String username, String password) {
                authorizeAndCallback(username, password, callback);
            }
        });
        
    }

    public String getAuthorizationPrefix() {
        return "GoogleLogin auth=";
    }
    
    
    private void authorizeAndCallback(String username, String password, GoogleCloudPrintPlugin.AuthorizeCallback callback) {
        try {
            String authCode = "";

            URL url = new URL("https://www.google.com/accounts/ClientLogin?accountType=HOSTED_OR_GOOGLE&Email=" + username + "&Passwd=" + password + "&service=cloudprint&source=" + this.source);

            String responseContent = readAll(url.openStream());

            String[] split = responseContent.split("\n");
            for (String s : split) {
                String[] nvsplit = s.split("=");
                if (nvsplit.length == 2) {
                    if (nvsplit[0].equals("Auth")) {
                        authCode = nvsplit[1];
                        callback.onAuthorized(authCode);
                    }
                }
            }

        } catch (IOException ex) {
            throw new PrinterException(ex);
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
