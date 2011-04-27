package ca.jbrains.auction.test;

import com.objogate.wl.swing.AWTEventQueueProber;
import com.objogate.wl.swing.driver.*;
import com.objogate.wl.swing.gesture.GesturePerformer;

import static org.hamcrest.Matchers.equalTo;

public class AuctionSniperDriver extends JFrameDriver {
    @SuppressWarnings("unchecked")
    public AuctionSniperDriver(int timeoutInMilliseconds) {
        super(new GesturePerformer(), JFrameDriver.topLevelFrame(
                named(Main.MAIN_WINDOW_NAME), showingOnScreen()),
                new AWTEventQueueProber(timeoutInMilliseconds, 100));
    }

    @SuppressWarnings("unchecked")
    public void showsSniperStatus(String text) {
        new JLabelDriver(this, named(Main.SNIPER_STATUS_NAME))
                .hasText(equalTo(text));
    }
}
