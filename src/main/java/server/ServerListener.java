package server;

import global.AbstractListener;
import global.ConnectionData;
import global.protocol.ChatMessage;
import global.protocol.ClientJoinMessage;
import global.protocol.ClientLeaveMessage;
import global.protocol.Message;
import global.protocol.game.GameMessage;

public class ServerListener extends AbstractListener {
    private final Server server;

    public ServerListener(ConnectionData proxyServer, Server server) {
        super(proxyServer);
        this.server = server;
    }

    @Override
    public void processMessage(Message message) {
        switch (message) {
            case ChatMessage chatMessage -> {
                System.out.println("Forwarding message from " + chatMessage.sender);
                send(chatMessage, connectionData);
                server.getServerData().addChatMessage(chatMessage.sender + ": " + chatMessage.message);
            }
            case ClientJoinMessage clientJoinMessage -> {
                System.out.println(clientJoinMessage.username + " has joined the server.");
                send(clientJoinMessage, connectionData);
                server.getUsernames().add(clientJoinMessage.username);
            }
            case ClientLeaveMessage clientLeaveMessage -> {
                System.out.println(clientLeaveMessage.username + " has left the server.");
                send(clientLeaveMessage, connectionData);
                server.getUsernames().remove(clientLeaveMessage.username);
            }
            case GameMessage gameMessage -> server.getSelectedGame().processMessage(gameMessage);
            default -> System.out.printf("'got a %s, idk what it is tho...' - titan toiletmaster\n", message.getClass());
        }
    }
}
