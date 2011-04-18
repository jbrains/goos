package ca.jbrains.auction;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

public class Main {
	private static final String AUCTION_RESOURCE_NAME = "Auction";

	public static class MainWindow extends JFrame {
		private static final long serialVersionUID = -6155423004752976439L;

		public static final String STATUS_LOST = "Lost";
		public static final String STATUS_JOINING = "Joining";
		public static final String STATUS_BIDDING = "Bidding";

		private JLabel sniperStatus = createLabel(MainWindow.STATUS_JOINING);

		public MainWindow() {
			super("Auction Sniper");
			setName(MAIN_WINDOW_NAME);
			add(sniperStatus);
			pack();
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		}

		private static JLabel createLabel(String text) {
			final JLabel label = new JLabel(text);
			label.setName("sniper status");
			label.setBorder(new LineBorder(Color.BLACK));
			return label;
		}

		public void showStatus(String status) {
			sniperStatus.setText(status);
		}
	}

	public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
	public static final String SNIPER_STATUS_NAME = "sniper status";
	public static final String ITEM_ID_AS_LOGIN = "auction-%s";
	private static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN
			+ "@%s/Auction";
	private MainWindow ui;

	@SuppressWarnings("unused")
	private Chat dontGcMeBro;

	public Main() throws Exception {
		startUserInterface();
	}

	private void startUserInterface() throws Exception {
		SwingUtilities.invokeAndWait(new Runnable() {
			@Override
			public void run() {
				ui = new MainWindow();
			}
		});
	}

	public static void main(final String... args) throws Exception {
		Main main = new Main();

		@SuppressWarnings("serial")
		Map<String, String> arguments = new HashMap<String, String>() {
			{
				put("hostname", args[0]);
				put("username", args[1]);
				put("password", args[2]);
				put("itemId", args[3]);
			}
		};

		main.joinAuction(
				connectTo(arguments.get("hostname"), arguments.get("username"),
						arguments.get("password")), arguments.get("itemId"));
	}

	private void joinAuction(final XMPPConnection connection, String itemId)
			throws XMPPException {

		final Chat chat = connection.getChatManager().createChat(
				auctionId(itemId, connection), new MessageListener() {
					@Override
					public void processMessage(Chat chat, Message message) {
						ui.showStatus(MainWindow.STATUS_LOST);
					}
				});

		this.dontGcMeBro = chat;

		chat.sendMessage(new Message());
	}

	private static String auctionId(String itemId, XMPPConnection connection) {
		return String.format(AUCTION_ID_FORMAT, itemId,
				connection.getServiceName());
	}

	private static XMPPConnection connectTo(String hostname, String username,
			String password) throws XMPPException {

		final XMPPConnection connection = new XMPPConnection(hostname);
		connection.connect();
		connection.login(username, password, AUCTION_RESOURCE_NAME);
		return connection;
	}
}
