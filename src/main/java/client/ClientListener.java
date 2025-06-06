package client;

import client.game.Cube;
import global.AbstractListener;
import global.ConnectionData;
import global.protocol.*;
import global.protocol.game.jumpincremental.PlayerData;
import global.protocol.game.jumpincremental.UpdateStateMessage;

import java.util.Arrays;
import java.util.Objects;

public class ClientListener extends AbstractListener {
    private Client client;

    private boolean timerRunning = false;

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
                }
            }
            case ClientLeaveMessage clientLeaveMessage -> {
                client.chatArea.append(clientLeaveMessage.toString());
            }

            case ClientJoinMessage clientJoinMessage -> {
                if (!(Objects.equals(clientJoinMessage.username, client.username))) client.chatArea.append(clientJoinMessage.toString());
            }

            case UpdateStateMessage updateStateMessage -> handleJumpIncrementalStateUpdate(updateStateMessage);


            default -> System.out.println("Received unknown message - Britain BoiletBaster " + message.getClass());
        }
    }

    private void handleJumpIncrementalStateUpdate(UpdateStateMessage stateMessage) {
        if (client.isInGame() && !timerRunning) {
            client.startJumpIncrementalTimer();
            timerRunning = true;
        }
        for (PlayerData playerData : stateMessage.data) {
            client.canvas.cubes.stream().filter(cube -> cube.getUsername().equals(playerData.name)).findFirst().ifPresentOrElse(
                    cube -> {
                        cube.setFromPlayer(playerData);
                    }, () -> {
                        System.out.println("NEW CUBE: " + playerData.name);
                        Cube c = new Cube(playerData);
                        client.canvas.addCube(c);
                    });
        }
    }

}
