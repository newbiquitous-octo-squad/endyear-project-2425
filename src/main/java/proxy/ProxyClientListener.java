package proxy;

import global.ClientConnectionData;
import global.ConnectionData;
import global.protocol.ChatMessage;
import global.protocol.ClientJoinMessage;
import global.protocol.ClientLeaveMessage;
import global.protocol.Message;
import global.protocol.central.ConnectionDeclinedMessage;

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

    // Sends the message to all the clients
//    protected void broadcast(Message m) {
//        clientList.forEach(client -> send(m, client));
//    }

    @Override
    public void onDisconnect() {
        clientList.remove(connectionData);
    }

    @Override
    public void processMessage(Message message) {
        switch (message) {
            case ClientJoinMessage clientJoinMessage -> {
                if (clientList.stream().anyMatch(data -> clientJoinMessage.username.equals(data.getName()))) {
                    System.out.println("kicking client for dupe user");
                    send(new ConnectionDeclinedMessage("Your username was already taken! Find a new one."), connectionData);
                    this.close();
                    return;
                }
                ((ClientConnectionData) connectionData).setName(clientJoinMessage.username);
                send(clientJoinMessage);
            }
            case ChatMessage chatMessage -> {
                if (((ClientConnectionData) connectionData).getName() == null) {
                    System.err.println("How?");
                    return;
                }
                send(chatMessage);
            }
            default -> send(message);
        }
    }
}
