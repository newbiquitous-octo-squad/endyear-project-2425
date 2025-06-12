package server;

import global.ConnectionData;
import global.GameType;
import global.Sender;
import global.protocol.Message;
import global.protocol.PingMessage;
import global.protocol.ServerStartupInfoMessage;
import global.ServerData;
import global.protocol.game.GameStartedMessage;
import proxy.Proxy;
import server.game.Game;
import server.game.jumpincremental.JumpIncremental;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Server implements Runnable {
    private ServerData serverData;
    private final List<String> usernames = Collections.synchronizedList(new ArrayList<>());
    private Game selectedGame;
    private volatile boolean running = true;
    private ConnectionData proxyConnectionData;
    private ServerListener listener;

    public Server(ServerData serverData) {
        this.serverData = serverData;
        serverData.setServer(this);
        try {
            Socket socket = new Socket(Proxy.HOST, Proxy.PORT);
            proxyConnectionData = new ConnectionData(socket);
        } catch (IOException e) {
            System.err.println("Seerver couldn't the be! Why? aaaa");
            e.printStackTrace();
            System.err.println("More robust logging");
        }

    }

    @Override
    public void run() {
        listener = new ServerListener(proxyConnectionData, this);
        new Thread(listener).start();
        Sender.send(new ServerStartupInfoMessage(serverData), proxyConnectionData);
        System.out.println(this.serverData.name);

        // send a ping whenever the user types anything
        Scanner s = new Scanner(System.in);
        while (running) {
            s.nextLine();
            Sender.send(new PingMessage(), proxyConnectionData);
        }
    }

    public List<String> getUsernames() {
        return usernames;
    }

    public Game getSelectedGame() {
        return selectedGame;
    }

    public void startGame(GameType game) {
        selectedGame = switch (game) {
            case JUMP_INCREMENTAL -> new JumpIncremental(proxyConnectionData);
        };
        Sender.send(new GameStartedMessage(game), proxyConnectionData);
        new Thread(selectedGame).start();
    }

    public void stop() {
        running = false;
        if (getSelectedGame() != null) getSelectedGame().stop();
    }

    public ServerData getServerData() {
        return serverData;
    }

    public void handleMessage(Message message) {
        listener.processMessage(message);
    }
}
