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

	@BeforeClass
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
	public void authenticateWithSasl() throws Exception {
		final String username = "sniper";
		final String password = "sniper";
		final String serviceName = "localhost";
		
		final ConnectionConfiguration config = new ConnectionConfiguration(
				serviceName);
		config.setCompressionEnabled(false);
		config.setSASLAuthenticationEnabled(true);
		config.setDebuggerEnabled(XMPPConnection.DEBUG_ENABLED);

		xmppConnection = new XMPPConnection(config);
		xmppConnection.connect();
		assertTrue(xmppConnection.isConnected());
		assertFalse(xmppConnection.isAuthenticated());

		assertTrue(config.isSASLAuthenticationEnabled());

		final SASLAuthentication saslAuthentication = xmppConnection
				.getSASLAuthentication();
		assertTrue(saslAuthentication.hasNonAnonymousAuthentication());
		assertNotNull(password);

		SASLAuthentication.supportSASLMechanism("PLAIN", 0);
		final String response = saslAuthentication.authenticate(username,
				password, serviceName);
		assertNotNull(response);
	}

	@Test
	public void authenticateWithoutSasl() throws Exception {
		final String username = "sniper";
		final String password = "sniper";
		final String serviceName = "localhost";

		final ConnectionConfiguration config = new ConnectionConfiguration(
				serviceName);
		config.setCompressionEnabled(false);
		config.setSASLAuthenticationEnabled(false);
		config.setDebuggerEnabled(XMPPConnection.DEBUG_ENABLED);

		xmppConnection = new XMPPConnection(config);
		xmppConnection.connect();
		assertTrue(xmppConnection.isConnected());
		assertFalse(xmppConnection.isAuthenticated());

		try {
			new VisibleNonSASLAuthentication(xmppConnection)
					.authenticate(username, password, serviceName);
			fail("How did non-SASL authentication work with a server that demands it!?");
		} catch (XMPPException expected) {
		}
	}

	@Ignore("Waiting for the Saff Squeeze to help")
	@Test
	public void login() throws Exception {
		final XMPPConnection xmppConnection = new XMPPConnection("localhost");
		xmppConnection.connect();
		xmppConnection.login("jbrains", "jbra1ns", "localhost");
	}
}
