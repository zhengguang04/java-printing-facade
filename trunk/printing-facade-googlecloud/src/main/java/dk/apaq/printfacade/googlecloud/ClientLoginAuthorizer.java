package dk.apaq.printfacade.googlecloud;

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

    private final String username;
    private final String password;
    private final String source;

    public ClientLoginAuthorizer(String username, String password, String source) {
        this.username = username;
        this.password = password;
        this.source = source;
    }
    
    public String authorize() {
        try {
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
