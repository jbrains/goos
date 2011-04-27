package ca.jbrains.auction.learning.test;

import static org.junit.Assert.*;

import org.jivesoftware.smack.*;
import org.junit.*;

public class LearnToAuthenticateOnEjabberdTest {
    private XMPPConnection xmppConnection;
    private final String username = "sniper";
    private final String password = "sniper";
    private final String serviceName = "localhost";

    @After
    public void disconnectIfNeeded() throws Exception {
        if (xmppConnection != null)
            xmppConnection.disconnect();
    }

    @Test
    public void authenticateWithSasl() throws Exception {
        final ConnectionConfiguration config = configureWithSaslAuthenticationEnabled(true);

        xmppConnection = new XMPPConnection(config);
        xmppConnection.connect();
        assertTrue(xmppConnection.isConnected());
        assertFalse(xmppConnection.isAuthenticated());

        assertTrue(config.isSASLAuthenticationEnabled());

        final SASLAuthentication saslAuthentication = xmppConnection
                .getSASLAuthentication();
        assertTrue(saslAuthentication.hasNonAnonymousAuthentication());
        assertNotNull(password);

        SaslAuthenticationConfiguration
                .ensureSaslAuthenticationMechanismIsAppliedFirst("PLAIN");
        final String response = saslAuthentication.authenticate(username,
                password, serviceName);
        assertNotNull(response);
    }

    private ConnectionConfiguration configureWithSaslAuthenticationEnabled(
            boolean saslAuthenticationEnabled) {
        final ConnectionConfiguration config = new ConnectionConfiguration(
                serviceName);
        config.setCompressionEnabled(false);
        config.setSASLAuthenticationEnabled(saslAuthenticationEnabled);
        config.setDebuggerEnabled(XMPPConnection.DEBUG_ENABLED);
        return config;
    }

    @Test
    public void authenticateWithoutSasl() throws Exception {
        final ConnectionConfiguration config = configureWithSaslAuthenticationEnabled(false);

        xmppConnection = new XMPPConnection(config);
        xmppConnection.connect();
        assertTrue(xmppConnection.isConnected());
        assertFalse(xmppConnection.isAuthenticated());

        try {
            new VisibleNonSASLAuthentication(xmppConnection).authenticate(
                    username, password, serviceName);
            fail("How did non-SASL authentication work with a server that demands it!?");
        } catch (XMPPException expected) {
        }
    }
}
