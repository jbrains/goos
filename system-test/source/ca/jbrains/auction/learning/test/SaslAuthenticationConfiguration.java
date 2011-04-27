package ca.jbrains.auction.learning.test;

import org.jivesoftware.smack.SASLAuthentication;

public class SaslAuthenticationConfiguration {
    public static void ensureSaslAuthenticationMechanismIsAppliedFirst(
            String saslAuthenticationMechanismAsText) {

        SASLAuthentication.supportSASLMechanism(
                saslAuthenticationMechanismAsText, 0);
    }
}
