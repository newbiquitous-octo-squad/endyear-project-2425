package proxy;

import global.ConnectionData;
import global.protocol.Message;

import java.util.List;

public class ProxyClientListener extends AbstractProxyListener {
    private ConnectionData server;
    public ProxyClientListener(ConnectionData clientData, ConnectionData server, List<ConnectionData> otherClientsList) {
        super(clientData, otherClientsList);
        this.server = server;
    }

    @Override
    public void run() {
        while (!isClosed) {
            readMessage().ifPresent(this::send);
        }
    }

    protected void send(Message m) {
        super.send(m, server);
    }


    @Override
    public void onDisconnect() {
        clientList.remove(connectionData);
    }
}
