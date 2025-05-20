package proxy;

import protocol.Message;

import java.util.List;

public class ProxyServerListener extends AbstractProxyListener {
    private final List<ClientData> clientList;
    public ProxyServerListener(ClientData clientData, List<ClientData> clientList) {
        super(clientData);
        this.clientList = clientList;
    }

    public void broadcast(Message message) {
        for (ClientData client : clientList) {
            send(message, client);
        }
    }

    @Override
    public void run() {
        while (true) {
            readMessage().ifPresent(this::broadcast);
        }
    }

    @Override
    public void onDisconnect() {
        // TODO: TRANSFER SERVER
    }
}
