package server.game;

import global.protocol.game.GameMessage;

public abstract class Game implements Runnable {
    public volatile boolean running = false;
    public abstract void processMessage(GameMessage m);
    public abstract void stop();
    public abstract <T extends GameData> T getGameData();
}
