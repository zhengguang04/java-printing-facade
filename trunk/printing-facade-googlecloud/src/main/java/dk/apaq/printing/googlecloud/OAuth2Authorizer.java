package dk.apaq.printing.googlecloud;

import com.google.gson.Gson;
import dk.apaq.printing.core.PrinterException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author krog
 */
public class OAuth2Authorizer implements GoogleCloudPrintPlugin.Authorizer {

    private static final Logger LOG = LoggerFactory.getLogger(OAuth2Authorizer.class);
    private static final String tokenUrl = "https://accounts.google.com/o/oauth2/token";
    
    private final String clientId;
    private final String clientSecret;
    private final String authorizationCode;
    private AccessTokenInfo accessTokenInfo;
    private Date lastTokenUpdate;
    private Gson gson = new Gson();
    
    protected static class AccessTokenInfo {

        private String accessToken;
        private int expiresIn;
        private String tokenType;
        private String refresh_token;

        public AccessTokenInfo(String accessToken, int expiresIn, String tokenType, String refresh_token) {
            this.accessToken = accessToken;
            this.expiresIn = expiresIn;
            this.tokenType = tokenType;
            this.refresh_token = refresh_token;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public int getExpiresIn() {
            return expiresIn;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public String getTokenType() {
            return tokenType;
        }

        
    }

    public OAuth2Authorizer(String clientId, String clientSecret, String authorizationCode) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.authorizationCode = authorizationCode;
    }
    
    
    public void authorize(GoogleCloudPrintPlugin.AuthorizeCallback callback) {
        callback.onAuthorized(getAccessToken());
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
                writer.write("&refresh_token="+accessTokenInfo.refresh_token);
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
