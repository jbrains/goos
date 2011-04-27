package ca.jbrains.auction.learning.test;

import static org.junit.Assert.*;

import org.jivesoftware.smack.*;
import org.jmock.Expectations;
import org.jmock.integration.junit4.*;
import org.junit.*;
import org.junit.runner.RunWith;

@RunWith(JMock.class)
public class LearnToConnectToEjabberdTest {
	private JUnit4Mockery mockery = new JUnit4Mockery();
	private XMPPConnection xmppConnection;

	public static void debugSmack() {
		System.setProperty("smack.debugEnabled", "true");
		XMPPConnection.DEBUG_ENABLED = true;
		SmackConfiguration.setPacketReplyTimeout(60000);
	}

	public void pause() throws Exception {
		Thread.sleep(10000L);
	}

	@After
	public void disconnectIfNeeded() throws Exception {
		if (xmppConnection != null)
			xmppConnection.disconnect();
	}
	
	@Test
	@Ignore
	public void connectThenDisconnect() throws Exception {
		final ConnectionListener connectionListener = mockery
				.mock(ConnectionListener.class);

		mockery.checking(new Expectations() {
			{
				oneOf(connectionListener).connectionClosed();
			}
		});

		xmppConnection = new XMPPConnection("localhost");
		xmppConnection.connect();
		xmppConnection.addConnectionListener(connectionListener);
		xmppConnection.disconnect(); // SMELL Move this to another test?
	}

	@Test
	public void login() throws Exception {
		final XMPPConnection xmppConnection = new XMPPConnection("localhost");
		xmppConnection.connect();
		SaslAuthenticationConfiguration.ensureSaslAuthenticationMechanismIsAppliedFirst("PLAIN");
		xmppConnection.login("sniper", "sniper", "localhost");
	}
	
	@Test
	public void saff() throws Exception {
		final TranslucentXmppConnection xmppConnection = new TranslucentXmppConnection("localhost");
		final ConnectionConfiguration configuration = xmppConnection.getConfiguration();
		assertTrue(configuration.isSASLAuthenticationEnabled());
		xmppConnection.connect();
		SaslAuthenticationConfiguration.ensureSaslAuthenticationMechanismIsAppliedFirst("PLAIN");
		assertTrue(xmppConnection.isConnected());
		assertFalse(xmppConnection.isAuthenticated());
		final SASLAuthentication saslAuthentication = xmppConnection.getSASLAuthentication();
		assertTrue(saslAuthentication.hasNonAnonymousAuthentication());
		
		configuration.setReconnectionAllowed(false);
		configuration.setSendPresence(false);
		
		assertEquals("sniper@localhost/localhost", saslAuthentication.authenticate("sniper", "sniper", "localhost"));
	}
}
