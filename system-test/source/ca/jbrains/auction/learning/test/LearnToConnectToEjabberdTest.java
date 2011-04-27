package ca.jbrains.auction.learning.test;

import org.jivesoftware.smack.*;
import org.junit.*;

public class LearnToConnectToEjabberdTest {
    private XMPPConnection xmppConnection;

    public static void debugSmack() {
        System.setProperty("smack.debugEnabled", "true");
        XMPPConnection.DEBUG_ENABLED = true;
        SmackConfiguration.setPacketReplyTimeout(60000);
    }

    @After
    public void disconnectIfNeeded() throws Exception {
        if (xmppConnection != null)
            xmppConnection.disconnect();
    }

    @Test
    public void login() throws Exception {
        final XMPPConnection xmppConnection = new XMPPConnection("localhost");
        xmppConnection.connect();
        SaslAuthenticationConfiguration
                .ensureSaslAuthenticationMechanismIsAppliedFirst("PLAIN");
        xmppConnection.login("sniper", "sniper", "localhost");
    }
}
