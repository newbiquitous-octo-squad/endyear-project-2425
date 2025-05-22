package proxy;

import global.AbstractListener;
import global.ConnectionData;

import java.util.List;

public abstract class AbstractProxyListener extends AbstractListener {
    protected final List<ConnectionData> clientList;
    public AbstractProxyListener(ConnectionData connectionData, List<ConnectionData> clientList) {
        super(connectionData);
        this.clientList = clientList;
    }

    public abstract void onDisconnect();

    @Override
    public void close() {
        onDisconnect();
        super.close();
    }
}
