package server;

import global.ConnectionData;
import global.GameType;
import global.Sender;
import global.protocol.Message;
import global.protocol.PingMessage;
import global.protocol.ServerStartupInfoMessage;
import global.ServerData;
import global.protocol.central.transfer.ElevateToHostMessage;
import global.protocol.game.GameStartedMessage;
import global.protocol.central.transfer.ServerShutdownMessage;
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

    public Server(ServerData serverData, boolean firstHost) {
        this.serverData = serverData;
        serverData.setServer(this);
        if (firstHost) {
            firstHost();
        } else {
            newHost();
        }
    }

    private void firstHost() {
        try {
            Socket socket = new Socket(Proxy.HOST, Proxy.PORT);
            proxyConnectionData = new ConnectionData(socket);
        } catch (IOException e) {
            System.err.println("Seerver couldn't the be! Why? aaaa");
            e.printStackTrace();
        }
    }

    private void newHost() {
        try {
            Socket socket = new Socket(Proxy.HOST, Proxy.SHARER_PORT);
            proxyConnectionData = new ConnectionData(socket);
            Sender.send(new ElevateToHostMessage(serverData.name), proxyConnectionData);
        } catch (IOException e) {
            System.err.println("Server couldn't be remade because of: ");
            e.printStackTrace();
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
        Sender.send(new ServerShutdownMessage(serverData), proxyConnectionData);
        running = false;
        getSelectedGame().stop();
        listener.close();
    }

    public ServerData getServerData() {
        return serverData;
    }

    public void handleMessage(Message message) {
        listener.processMessage(message);
    }
}
