package client;

import global.AbstractListener;
import global.ConnectionData;
import global.protocol.*;

import java.util.Objects;

import static client.Client.chatArea;
import static client.Client.username;

public class ClientListener extends AbstractListener {
    public ClientListener(ConnectionData connectionData) {
        super(connectionData);
    }

    @Override
    public void processMessage(Message message) {
        switch (message) {
            case PingMessage ignored -> {
                System.out.println("Received Ping message");
                send(new PongMessage(), connectionData);
            }

            case ChatMessage chatMessage -> {
                if (!chatMessage.sender.equals(username)) {
                    chatArea.append(chatMessage.toString());
                    System.out.println("Message appended!");
                }
            }
            case ClientLeaveMessage clientLeaveMessage -> {
                chatArea.append(clientLeaveMessage.toString());
            }

            case ClientJoinMessage clientJoinMessage -> {
                if (!(Objects.equals(clientJoinMessage.username, username))) chatArea.append(clientJoinMessage.toString());
            }
            default -> System.out.println("Received unknown message - Bitain BoiletBaster");
        }
    }
}
