package server.game.jumpincremental;

import global.ConnectionData;
import global.Sender;
import global.protocol.game.GameMessage;
import global.protocol.game.GameRegisterMessage;
import global.protocol.game.GameUnregisterMessage;
import global.protocol.game.jumpincremental.*;
import server.game.Game;

import java.util.*;

public class JumpIncremental extends Game {
    private List<PlayerData> players;
    private Timer timer = new Timer();

    public static int TICK_DELAY = 16;
    public static int FLOOR_HEIGHT = 400;
    public static int CANVAS_WIDTH = 700;

    public JumpIncremental(ConnectionData connectionData) {
        this.players = new ArrayList<>();
        running = true;
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Sender.send(new UpdateStateMessage(players), connectionData);
            }
        }, 100, TICK_DELAY);
    }

    @Override
    public void run() {
        while (running) {
            for (PlayerData playerData : players) {
                playerData.update();
            }
            System.out.println(players);
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
                stop();
            }
        }
        timer.cancel();
    }

    @Override
    public void processMessage(GameMessage m) {
        switch (m) {
            case GameRegisterMessage registerMessage -> {
                PlayerData newPlayer = new PlayerData(registerMessage.username);
                newPlayer.name = registerMessage.username;
                players.add(newPlayer);
                System.out.println("Ok? Now " + registerMessage.username + " is here");
                System.out.println("The new player list is: " + players.toString());
            }
            case GameUnregisterMessage unregisterMessage -> players.stream().filter(p -> p.name.equals(unregisterMessage.username)).findFirst().ifPresent(p -> players.remove(p));
            case ClientKeyMessage keyMessage -> handleKeyMessage(keyMessage);
            default ->
                    System.out.println("Saw a message that was called " + m.getClass().getSimpleName() + ", idk what that means tho. Taitan MoiletDisaster");
        }
    }


    public void handleKeyMessage(ClientKeyMessage keyMessage) {
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).name.equals(keyMessage.username)) {
                PlayerData player = players.get(i);
                if (keyMessage.hasKey('W')) {
                    if (player.y + player.size == FLOOR_HEIGHT) {
                        player.jump();
                    }
                }
                player.velocityX = keyMessage.hasKey('A') ? -30 : (keyMessage.hasKey('D') ? 30 : 0);

                players.set(i, player.copy());
            }
        }
    }

    @Override
    public void stop() {
        this.running = false;
    }

    @Override
    public JumpIncrementalData getGameData() {
        return new JumpIncrementalData(players);
    }
}