package proxy;

import global.ClientConnectionData;
import global.ConnectionData;
import global.protocol.ClientJoinMessage;
import global.protocol.Message;

import java.util.List;

public class ProxyClientListener extends AbstractProxyListener {
    private ConnectionData server;
    public ProxyClientListener(ClientConnectionData clientData, ConnectionData server, List<ClientConnectionData> otherClientsList) {
        super(clientData, otherClientsList);
        this.server = server;
    }


    protected void send(Message m) {
        super.send(m, server);
    }


    @Override
    public void onDisconnect() {
        clientList.remove(connectionData);
    }

    @Override
    public void processMessage(Message message) {
        switch (message) {
            case ClientJoinMessage clientJoinMessage:
                ((ClientConnectionData) connectionData).setName(clientJoinMessage.username);
            default:
                send(message);
        }
    }
}
