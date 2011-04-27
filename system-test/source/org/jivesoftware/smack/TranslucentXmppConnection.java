package org.jivesoftware.smack;

import javax.security.auth.callback.CallbackHandler;

public class TranslucentXmppConnection extends XMPPConnection {
    public TranslucentXmppConnection(String serviceName) {
        super(serviceName);
    }

    public TranslucentXmppConnection(ConnectionConfiguration config) {
        super(config);
    }

    public TranslucentXmppConnection(String serviceName,
            CallbackHandler callbackHandler) {
        super(serviceName, callbackHandler);
    }

    public TranslucentXmppConnection(ConnectionConfiguration config,
            CallbackHandler callbackHandler) {
        super(config, callbackHandler);
    }

    @Override
    public ConnectionConfiguration getConfiguration() {
        return super.getConfiguration();
    }
}
