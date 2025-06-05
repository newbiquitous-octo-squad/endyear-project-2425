package server.game.jumpincremental;

import global.ConnectionData;
import global.Sender;
import global.protocol.game.GameMessage;
import global.protocol.game.GameRegisterMessage;
import global.protocol.game.GameUnregisterMessage;
import global.protocol.game.jumpincremental.ClientJumpMessage;
import global.protocol.game.jumpincremental.ClientShareStateMessage;
import global.protocol.game.jumpincremental.PlayerData;
import global.protocol.game.jumpincremental.UpdateStateMessage;
import server.game.Game;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JumpIncremental extends Game {
    private ConnectionData connectionData;
    private List<PlayerData> players;
    private Timer timer = new Timer();

    public static int TICKDELAY = 1000;

    public JumpIncremental(ConnectionData connectionData) {
        this.players = new ArrayList<>();
        running = true;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Server: Sending these players: " + Arrays.toString(players.stream().map(p -> p.name).toArray()));
                Sender.send(new UpdateStateMessage(players), connectionData);
            }
        }, 100, TICKDELAY);
    }

    @Override
    public void run() {
        while (running) {
        }
        timer.cancel();
    }

    @Override
    public void processMessage(GameMessage m) {
        switch (m) {
            case GameRegisterMessage registerMessage -> {
                PlayerData newPlayer = new PlayerData();
                newPlayer.name = registerMessage.username;
                players.add(newPlayer);
                System.out.println("Ok? Now " + registerMessage.username + " is here");
                System.out.println("The new player list is: " + players.toString());
            }
            case GameUnregisterMessage unregisterMessage -> players.stream().filter(p -> p.name.equals(unregisterMessage.username)).findFirst().ifPresent(p -> players.remove(p));
            case ClientShareStateMessage stateMessage -> this.setPlayerData(stateMessage);
            case ClientJumpMessage jumpMessage -> players.stream().filter(playerData -> playerData.name.equals(jumpMessage.username)).findFirst().ifPresent(player -> player.score++);
            default ->
                    System.out.println("Saw a message that was called " + m.getClass().getSimpleName() + ", idk what that means tho. Taitan MoiletDisaster");
        }
    }

    public void setPlayerData(ClientShareStateMessage stateMessage) {
        players.stream().filter(player -> player.name.equals(stateMessage.name)).findFirst();
    }

    @Override
    public void stop() {
        this.running = false;
    }
}