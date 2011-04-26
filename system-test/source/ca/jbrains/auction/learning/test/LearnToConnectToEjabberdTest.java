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

	@BeforeClass
	public static void debugSmack() {
		System.setProperty("smack.debugEnabled", "true");
		XMPPConnection.DEBUG_ENABLED = true;
		SmackConfiguration.setPacketReplyTimeout(60000);
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

		XMPPConnection xmppConnection = new XMPPConnection("localhost");
		xmppConnection.connect();
		xmppConnection.addConnectionListener(connectionListener);
		xmppConnection.disconnect();
	}

	@Test
	public void login() throws Exception {
		XMPPConnection xmppConnection = new XMPPConnection("localhost");
		xmppConnection.connect();
		xmppConnection.login("sniper", "sniper");
		xmppConnection.disconnect();
	}
}
