package dk.apaq.printfacade.googlecloud;

import dk.apaq.printing.core.PrinterException;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;

/**
 *
 * @author krog
 */
public class ClientLoginAuthorizer implements GoogleCloudPrintPlugin.Authorizer {

    private String username;
    private String password;
    private final String source;

    public ClientLoginAuthorizer(String source) {
        this.source = source;
    }
    
    public class LoginForm extends javax.swing.JFrame {

    /** Creates new form LoginForm */
    public LoginForm() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setText("Login");
 
        jPasswordField1.setText("qwerrty");

        jLabel1.setText("Username");

        jLabel2.setText("Password");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(jLabel2)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jButton1)
                    .add(jTextField1)
                    .add(jPasswordField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 130, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(38, 38, 38)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jTextField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jPasswordField1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jButton1)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    
    public String getUsername() {
        return jTextField1.getText();
    }
    
    public String getPassword() {
        return new String(jPasswordField1.getPassword());
    }
    
    public JButton getButton() {
        return jButton1;
    }
}

    
    public ClientLoginAuthorizer(String username, String password, String source) {
        this.username = username;
        this.password = password;
        this.source = source;
    }
    
    public void authorize(final GoogleCloudPrintPlugin.AuthorizeCallback callback) {
        if(username!=null && password!=null) {
            authorizeAndCallback(callback);
        } else {
            final LoginForm form = new LoginForm();
            form.setVisible(true);
            
            form.getButton().addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent ae) {
                    username = form.getUsername();
                    password = form.getPassword();
                    authorizeAndCallback(callback);
                    form.setVisible(false);
                }
            });
            
        }
    }
    
    private void authorizeAndCallback(GoogleCloudPrintPlugin.AuthorizeCallback callback) {
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
