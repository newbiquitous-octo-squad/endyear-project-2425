package proxy;

import global.ConnectionData;
import global.protocol.Message;

public class ProxyClientListener extends AbstractProxyListener {
    private ConnectionData server;
    public ProxyClientListener(ConnectionData clientData, ConnectionData server) {
        super(clientData);
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
        // TODO: HANDLE CLIENT DISCONNECT
    }
}
