package server;

import global.AbstractListener;
import global.ConnectionData;
import global.protocol.ChatMessage;
import global.protocol.ClientJoinMessage;
import global.protocol.ClientLeaveMessage;
import global.protocol.Message;

public class ServerListener extends AbstractListener {

    public ServerListener(ConnectionData proxyServer) {
        super(proxyServer);
    }

    @Override
    public void processMessage(Message message) {
        switch (message) {
            case ChatMessage chatMessage -> {
                System.out.println("Forwarding message from " + chatMessage.sender);
                send(chatMessage, connectionData);
            }
            case ClientJoinMessage clientJoinMessage -> {
                System.out.println(clientJoinMessage.username + " has joined the server.");
                send(clientJoinMessage, connectionData);
            }
            case ClientLeaveMessage clientLeaveMessage -> {
                System.out.println(clientLeaveMessage.username + " has left the server.");
                send(clientLeaveMessage, connectionData);
            }
            default -> System.out.printf("'got a %s, idk what it is tho...' - titan toiletmaster\n", message.getClass());
        }
    }
}
