package dk.apaq.printing.googlecloud;

import com.google.gson.Gson;
import dk.apaq.printing.core.PrinterException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
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
    private final String redirectUri;
    private AccessTokenInfo accessTokenInfo;
    private Date lastTokenUpdate;
    private Gson gson = new Gson();
    private final AuthorizationCodeSupplier codeSupplier;

    public interface AuthorizationCodeSupplier {

        void retrieveCode(String clientId, String clientSecret, String redirectUri, AuthorizationCodeCallback callback);
    }

    public interface AuthorizationCodeCallback {

        void onCodeRetrieved(String code);
    }

    private static class SimpleSupplier implements AuthorizationCodeSupplier {

        private final String code;

        public SimpleSupplier(String code) {
            this.code = code;
        }

        public void retrieveCode(String clientId, String clientSecret, String redirectUri, AuthorizationCodeCallback callback) {
            callback.onCodeRetrieved(code);
        }
    }

    protected static class AccessTokenInfo {

        private String access_token;
        private int expires_in;
        private String token_type;
        private String refresh_token;

        public AccessTokenInfo(String accessToken, int expiresIn, String tokenType, String refresh_token) {
            this.access_token = accessToken;
            this.expires_in = expiresIn;
            this.token_type = tokenType;
            this.refresh_token = refresh_token;
        }

        public String getAccessToken() {
            return access_token;
        }

        public int getExpiresIn() {
            return expires_in;
        }

        public String getRefresh_token() {
            return refresh_token;
        }

        public String getTokenType() {
            return token_type;
        }
    }

    public OAuth2Authorizer(String clientId, String clientSecret, String redirectUri, String authorizationCode) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.codeSupplier = new SimpleSupplier(authorizationCode);
    }

    public OAuth2Authorizer(String clientId, String clientSecret, String redirectUri, AuthorizationCodeSupplier authorizationCodeSupplier) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;                
        this.codeSupplier = authorizationCodeSupplier;
    }

    public void authorize(final GoogleCloudPrintPlugin.AuthorizeCallback callback) {
        if (!isTokenExpired()) {
            callback.onAuthorized(accessTokenInfo.access_token);
        } else {
            codeSupplier.retrieveCode(clientId, clientSecret, redirectUri, new AuthorizationCodeCallback() {

                public void onCodeRetrieved(String code) {
                    HttpClient httpclient = null;
                    HttpResponse response = null;
                    try {

                        httpclient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(tokenUrl);

                        StringBody codePart = new StringBody(code);
                        StringBody clientIdPart = new StringBody(clientId);
                        StringBody clientSecretPart = new StringBody(clientSecret);

                        MultipartEntity reqEntity = new MultipartEntity();
                        reqEntity.addPart("code", codePart);
                        reqEntity.addPart("client_id", clientIdPart);
                        reqEntity.addPart("client_secret", clientSecretPart);

                        if (accessTokenInfo == null) {
                            reqEntity.addPart("grant_type", new StringBody("authorization_code"));
                            reqEntity.addPart("redirect_uri", new StringBody(redirectUri));
                        } else {
                            reqEntity.addPart("grant_type", new StringBody("refresh_token"));
                            reqEntity.addPart("redirect_token", new StringBody(accessTokenInfo.refresh_token));
                        }

                        httpPost.setEntity(reqEntity);

                        response = httpclient.execute(httpPost);

                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        response.getEntity().writeTo(bos);

                        accessTokenInfo = gson.fromJson(new InputStreamReader(new ByteArrayInputStream(bos.toByteArray())), AccessTokenInfo.class);

                        LOG.info("Got new token from Google: " + accessTokenInfo.access_token);
                        callback.onAuthorized(accessTokenInfo.access_token);
                    } catch (IOException ex) {
                        String message = "Could not retrieve access token from Google. ";
                        if (response != null) {
                            message += " " + response.getStatusLine();
                        }
                        throw new PrinterException(message, ex);
                    }

                }
            });
        }


    }

    public String getAuthorizationPrefix() {
        return "Bearer ";
    }

    private boolean isTokenExpired() {
        if (lastTokenUpdate == null || (System.currentTimeMillis() - lastTokenUpdate.getTime()) >= (accessTokenInfo.expires_in * 1000)) {
            return true;
        } else {
            return false;
        }
    }
}
