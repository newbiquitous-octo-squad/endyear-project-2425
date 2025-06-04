package client;

import client.game.Cube;
import global.AbstractListener;
import global.ConnectionData;
import global.protocol.*;
import global.protocol.game.jumpincremental.ClientShareStateMessage;
import global.protocol.game.jumpincremental.UpdateStateMessage;
import server.game.jumpincremental.Player;

import java.util.Objects;

public class ClientListener extends AbstractListener {
    private Client client;
    public ClientListener(ConnectionData connectionData, Client client) {
        super(connectionData);
        this.client = client;
    }

    @Override
    public void processMessage(Message message) {
        switch (message) {
            case PingMessage ignored -> {
                System.out.println("Received Ping message");
                send(new PongMessage(), connectionData);
            }

            case ChatMessage chatMessage -> {
                if (!chatMessage.sender.equals(client.username)) {
                    client.chatArea.append(chatMessage.toString());
                    System.out.println("Message appended!");
                }
            }
            case ClientLeaveMessage clientLeaveMessage -> {
                client.chatArea.append(clientLeaveMessage.toString());
            }

            case ClientJoinMessage clientJoinMessage -> {
                if (!(Objects.equals(clientJoinMessage.username, client.username))) client.chatArea.append(clientJoinMessage.toString());
            }

            case UpdateStateMessage updateStateMessage -> handleJumpIncrementalStateUpdate(updateStateMessage);


            default -> System.out.println("Received unknown message - Britain BoiletBaster");
        }
    }

    private void handleJumpIncrementalStateUpdate(UpdateStateMessage stateMessage) {
        client.canvas.cubes.forEach(cube -> {
            cube.setFromPlayer(stateMessage.data.get(cube.getUsername()));
            stateMessage.data.remove(cube.getUsername());
            System.out.println("Resetting cube state");
        });
        stateMessage.data.forEach((name, player) -> {
            Cube cube = new Cube(name);
            cube.setFromPlayer(player);
            client.canvas.cubes.add(cube);
        });
    }
}
