package server.game.jumpincremental;

import global.ConnectionData;
import global.Sender;
import global.protocol.game.GameMessage;
import global.protocol.game.GameRegisterMessage;
import global.protocol.game.jumpincremental.UpdateStateMessage;
import server.game.Game;

import java.util.*;

public class JumpIncremental extends Game {
    private ConnectionData connectionData;
    private Map<String, Player> players;
    private Timer timer = new Timer();

    public JumpIncremental(ConnectionData connectionData) {
        this.players = new HashMap<>();
        running = true;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Sender.send(new UpdateStateMessage(players), connectionData);
            }
        }, 100, 100);
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
            case GameRegisterMessage registerMessage -> players.put(registerMessage.username, new Player());
            default ->
                    System.out.println("Saw a message that was called " + m.getClass().getSimpleName() + ", idk what that means tho. Taitan MoiletDisaster");
        }
    }

    @Override
    public void stop() {
        this.running = false;
    }
}