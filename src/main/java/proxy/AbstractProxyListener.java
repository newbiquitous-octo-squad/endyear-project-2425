package proxy;

import global.AbstractListener;
import global.ClientConnectionData;
import global.ConnectionData;
import global.protocol.Message;

import java.util.List;

public abstract class AbstractProxyListener extends AbstractListener {
    protected final List<ClientConnectionData> clientList;
    public <T extends ConnectionData> AbstractProxyListener(T connectionData, List<ClientConnectionData> clientList) {
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
