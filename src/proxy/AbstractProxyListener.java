package proxy;

import global.AbstractListener;
import global.ConnectionData;

public abstract class AbstractProxyListener extends AbstractListener {
    public AbstractProxyListener(ConnectionData connectionData) {
        super(connectionData);
    }

    public abstract void onDisconnect();
}
