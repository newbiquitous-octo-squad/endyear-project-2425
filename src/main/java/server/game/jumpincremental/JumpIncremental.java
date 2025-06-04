package server.game.jumpincremental;

import global.ConnectionData;
import global.Sender;
import global.protocol.game.GameMessage;
import global.protocol.game.GameRegisterMessage;
import global.protocol.game.jumpincremental.ClientJumpMessage;
import global.protocol.game.jumpincremental.ClientShareStateMessage;
import global.protocol.game.jumpincremental.UpdateStateMessage;
import server.game.Game;

import java.util.*;

public class JumpIncremental extends Game {
    private ConnectionData connectionData;
    private Map<String, Player> players;
    private Timer timer = new Timer();

    public static int TICKDELAY = 1000;

    public JumpIncremental(ConnectionData connectionData) {
        this.players = new HashMap<>();
        running = true;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
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
                players.put(registerMessage.username, new Player());
                System.out.println("Ok? Now " + registerMessage.username + "is here");
            }
            case ClientShareStateMessage stateMessage -> this.setPlayerData(stateMessage);
            case ClientJumpMessage jumpMessage -> players.get(jumpMessage.username).score++;
            default ->
                    System.out.println("Saw a message that was called " + m.getClass().getSimpleName() + ", idk what that means tho. Taitan MoiletDisaster");
        }
    }

    public void setPlayerData(ClientShareStateMessage stateMessage) {
        System.out.println("This is have been called? - setplayerdat a- britain oybruvinnit master");
        Player p = players.get(stateMessage.name);
        if (!players.containsKey(stateMessage.name)) {
            p = new Player();
            players.put(stateMessage.name, p);
        }
        // print map??
        players.forEach( (username, player) -> {
            System.out.println(username);
        });
        p.accX = stateMessage.accX;
        p.accY = stateMessage.accY;
        p.velX = stateMessage.velX;
        p.velY = stateMessage.velY;
        p.posX = stateMessage.posX;
        p.posY = stateMessage.posY;
    }

    @Override
    public void stop() {
        this.running = false;
    }
}