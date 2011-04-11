package ca.jbrains.auction;

import static ca.jbrains.auction.ApplicationRunner.STATUS_JOINING;

import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;

public class Main {
	public static class MainWindow extends JFrame {
		private static final long serialVersionUID = -6155423004752976439L;

		private JLabel sniperStatus = createLabel(STATUS_JOINING);

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
	}

	public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
	public static final String SNIPER_STATUS_NAME = "sniper status";
	private MainWindow ui;

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

	public static void main(String... args) throws Exception {
		Main main = new Main();
	}
}
