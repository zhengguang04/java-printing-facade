package dk.apaq.printfacade.googlecloud;

import com.google.gson.Gson;
import dk.apaq.printing.core.AbstractPrinterManagerPlugin;
import dk.apaq.printing.core.Printer;
import dk.apaq.printing.core.PrinterException;
import dk.apaq.printing.core.PrinterJob;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author krog
 */
public class GoogleCloudPrintPlugin extends AbstractPrinterManagerPlugin {

    private static final Logger LOG = LoggerFactory.getLogger(GoogleCloudPrintPlugin.class);
    private static final String tokenUrl = "https://accounts.google.com/o/oauth2/token";
    /*
     * http://code.google.com/p/google-api-java-client/wiki/OAuth2Draft10#Sample_Program
     * http://code.google.com/apis/cloudprint/docs/appInterfaces.html
     * http://code.google.com/apis/accounts/docs/OAuth2.html#SS
     * 
     * code = 4/s0xkDRwfdiZR9rthiZdDCqKvekjP
     * clientid = 700939733854.apps.googleusercontent.com
     * clientSecret = yygJ1QE3yRsNieDoZKOKBUgl
     */
    private final String clientId;
    private final String clientSecret;
    private final String authorizationCode;
    private AccessTokenInfo accessTokenInfo;
    private Date lastTokenUpdate = null;
    private Gson gson = new Gson();

    private class AccessTokenInfo {

        private String accessToken;
        private int expiresIn;
        private String tokenType;
        private String refresh_token;
    }

    public GoogleCloudPrintPlugin(String clientId, String clientSecret, String authorizationCode) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationCode = authorizationCode;
    }

    public Printer getDefaultPrinter() {
        String token = getAccessToken();
        return null;
    }

    public List<Printer> getPrinters() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void print(PrinterJob job) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private String getAccessToken() throws PrinterException {
        
        if(!isTokenExpired()) {
            return accessTokenInfo.accessToken;
        }
        
        HttpURLConnection con = null;
        try {
            URL url = new URL(tokenUrl);
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            //con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write("code=" + authorizationCode);
            writer.write("&client_id=" + clientId);
            writer.write("&client_secret=" + clientSecret);
            
            if(accessTokenInfo == null) {
                writer.write("&grant_type=authorization_code");
                writer.write("&redirect_uri=http://localhost/oauth2callback");
            } else {
                writer.write("&grant_type=refresh_token");
                writer.write("&refresh_token="+accessTokenInfo.accessToken);
            }
            writer.close();

            accessTokenInfo = gson.fromJson(new InputStreamReader(con.getInputStream()), AccessTokenInfo.class);

            LOG.info("Got new token from Google: " + accessTokenInfo.accessToken);
            return accessTokenInfo.accessToken;
        } catch (IOException ex) {
            String message = "Could not retrieve access token from Google. ";
            if (con instanceof HttpURLConnection) {
                try {
                    message += " " + con.getResponseMessage();
                } catch (IOException ex1) {
                }
            }
            throw new PrinterException(message, ex);
        }

    }

    private boolean isTokenExpired() {
        if (lastTokenUpdate == null || (System.currentTimeMillis() - lastTokenUpdate.getTime()) >= (accessTokenInfo.expiresIn * 1000)) {
            return true;
        } else {
            return false;
        }
    }
}
