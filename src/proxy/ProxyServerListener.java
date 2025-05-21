package proxy;

import global.ConnectionData;
import global.protocol.Message;

import java.util.List;

public class ProxyServerListener extends AbstractProxyListener {
    private final List<ConnectionData> clientList;
    public ProxyServerListener(ConnectionData connectionData, List<ConnectionData> clientList) {
        super(connectionData);
        this.clientList = clientList;
    }

    public void broadcast(Message message) {
        for (ConnectionData client : clientList) {
            send(message, client);
        }
    }

    @Override
    public void run() {
        while (!isClosed) {
            readMessage().ifPresent(this::broadcast);
        }
    }

    @Override
    public void onDisconnect() {
        // TODO: TRANSFER SERVER
    }
}
