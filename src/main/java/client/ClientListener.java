package client;

import client.game.Cube;
import global.AbstractListener;
import global.ConnectionData;
import global.GameType;
import global.protocol.*;
import global.protocol.central.ConnectionDeclinedMessage;
import global.protocol.game.GameStartedMessage;
import global.protocol.game.jumpincremental.PlayerData;
import global.protocol.game.jumpincremental.UpdateStateMessage;

import javax.swing.*;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
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

            case ConnectionDeclinedMessage declinedMessage -> {
                System.out.println("server kicked");
                client.disconnect(declinedMessage.reason);
                close();
            }

            case ChatMessage chatMessage -> {
                if (!chatMessage.sender.equals(client.username)) {
                    client.chatArea.append(chatMessage.toString());
                }
            }
            case ClientLeaveMessage clientLeaveMessage -> {
                client.chatArea.append(clientLeaveMessage.toString());
                client.canvas.cubes.stream().filter(cube -> cube.getUsername().equals(clientLeaveMessage.username)).findFirst().ifPresent(cube -> client.canvas.cubes.remove(cube));
            }

            case ClientJoinMessage clientJoinMessage -> {
                if (!(Objects.equals(clientJoinMessage.username, client.username))) client.chatArea.append(clientJoinMessage.toString());
            }

            case GameStartedMessage gameStartedMessage -> client.beginGame(gameStartedMessage.gameType);


            case UpdateStateMessage updateStateMessage -> {
                if(!client.gameActive)
                    client.beginGame(GameType.JUMP_INCREMENTAL);
                    //this should be taken from updateStateMessage but for some reason our handling is structured like this
                handleJumpIncrementalStateUpdate(updateStateMessage);
            }


            default -> System.out.println("Received unknown message - Britain BoiletBaster " + message.getClass());
        }
    }

    private void handleJumpIncrementalStateUpdate(UpdateStateMessage stateMessage) {
        if (client.canvas == null) return; // rare case where we receive a message before we're ready for it
        if (client.isInGame() && !timerRunning) {
            client.startJumpIncrementalTimer();
            timerRunning = true;
        }
        for (PlayerData playerData : stateMessage.data) {
            client.canvas.cubes.stream().filter(cube -> cube.getUsername().equals(playerData.name)).findFirst().ifPresentOrElse(
                    cube -> cube.setFromPlayer(playerData), () -> {
                        System.out.println("NEW CUBE: " + playerData.name);
                        Cube c = new Cube(playerData);
                        client.canvas.addCube(c);
                    });
        }
    }

}
