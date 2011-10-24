/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.apaq.printing.googlecloud;

import dk.apaq.printing.googlecloud.GoogleCloudPrintPlugin.AuthorizeCallback;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author krog
 */
public class OAuth2AuthorizerTest {
    
    public OAuth2AuthorizerTest() {
    }

    
    /**
     * Test of authorize method, of class OAuth2Authorizer.
     */
    @Test
    public void testAuthorize() {
        System.out.println("authorize");
        AuthorizeCallback callback = new AuthorizeCallback() {

            public void onAuthorized(String authorizationCode) {
                System.out.println("code: "+authorizationCode);
            }
        };
        OAuth2Authorizer instance = new OAuth2Authorizer("700939733854.apps.googleusercontent.com", "TwM1ADz1REuRfd5muU89Rejv", "4/zA6-DKzBdmP2n1vsEqzFgHHU3HlE");
        instance.authorize(callback);
    }
}
