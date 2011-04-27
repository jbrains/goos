package org.jivesoftware.smack;

// SMELL I need this because Smack made NonSASLAuthentication package-visible
public class VisibleNonSASLAuthentication extends NonSASLAuthentication {
    public VisibleNonSASLAuthentication(XMPPConnection connection) {
        super(connection);
    }
}
