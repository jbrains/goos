package ca.jbrains.auction.test;

import java.awt.Color;
import java.awt.event.*;
import java.util.*;
import java.util.regex.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

import org.jivesoftware.smack.*;
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
    public static final String SNIPER_XMPP_ID = "sniper@localhost/Auction";

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

        disconnectWhenUiCloses(connection);
        final Chat chat = connection.getChatManager().createChat(
                auctionId(itemId, connection), new MessageListener() {
                    // SMELL This nested class makes cyclic dependencies too
                    // easy
                    @Override
                    public void processMessage(Chat chat, Message message) {
                        // REFACTOR Replace conditional with polymorphism
                        if (isReportPriceMessage(message)) {
                            // SMELL In one body, update the UI; in the other,
                            // send a new bid. That
                            // can't be right.
                            if (Main.SNIPER_XMPP_ID
                                    .equals(leadingBidderAccordingTo(message))) {
                                signalSniperIsBidding();
                            } else {
                                counterBid(chat);
                            }
                        } else if (isSniperBidMessage(message)) {
                            signalSniperIsBidding();
                        } else {
                            signalAuctionClosed();
                        }
                    }
                });

        this.dontGcMeBro = chat;

        // ASSUME This message doesn't need a specific body
        chat.sendMessage(new Message());
    }

    private void disconnectWhenUiCloses(final XMPPConnection connection) {
        ui.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent event) {
                connection.disconnect();
            }
        });
    }

    private static String auctionId(String itemId, XMPPConnection connection) {
        return String.format(AUCTION_ID_FORMAT, itemId,
                connection.getServiceName());
    }

    // REFACTOR Rename to loginTo().
    private static XMPPConnection connectTo(String hostname, String username,
            String password) throws XMPPException {

        final XMPPConnection connection = new XMPPConnection(hostname);
        connection.connect();
        connection.login(username, password, AUCTION_RESOURCE_NAME);
        return connection;
    }

    public static String leadingBidderAccordingTo(Message reportPriceMessage) {
        Matcher matcher = Pattern.compile(".*\\bBidder:([^;]*).*").matcher(
                reportPriceMessage.getBody());
        return matcher.matches() ? matcher.group(1).trim() : null;
    }

    private void counterBid(Chat chat) {
        try {
            chat.sendMessage("SOLVersion 1.1; Command: Bid; Price: 1098");
        } catch (XMPPException reported) {
            reported.printStackTrace();
        }
    }

    public static boolean isReportPriceMessage(Message message) {
        return messageBodyMatchesRegex(message, "SOLVersion:.*Event: PRICE.*");
    }

    private static boolean messageBodyMatchesRegex(Message message, String regex) {
        final String body = message.getBody();
        if (body == null)
            return false;

        return Pattern.compile(regex).matcher(body).matches();
    }

    public static boolean isSniperBidMessage(Message message) {
        return messageBodyMatchesRegex(message,
                "SOLVersion 1.1.*Command: Bid.*");
    }

    public void signalSniperIsBidding() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ui.showStatus(MainWindow.STATUS_BIDDING);
            }
        });
    }

    public void signalAuctionClosed() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                ui.showStatus(MainWindow.STATUS_LOST);
            }
        });
    }
}
