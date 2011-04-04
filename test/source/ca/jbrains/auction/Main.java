package ca.jbrains.auction;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Main {
	public static class MainWindow extends JFrame {
		public MainWindow() {
			super("Auction Sniper");
			setName(MAIN_WINDOW_NAME);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		}
	}

	public static final String MAIN_WINDOW_NAME = "Auction Sniper Main";
	public static final String SNIPER_STATUS_NAME = "sniper status name";
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
