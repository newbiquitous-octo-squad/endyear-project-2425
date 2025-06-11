package proxy;

import global.ClientConnectionData;
import global.ConnectionData;
import global.ServerData;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProxyInstance implements Runnable {
    private ConnectionData server;
    private final List<ClientConnectionData> clientList = Collections.synchronizedList(new ArrayList<>());
    private ExecutorService pool = Executors.newFixedThreadPool(1000);
    private int port;
    private ProxyServerListener listener;
    private boolean running;

    public ProxyInstance(ConnectionData server, int port) {
        this.server = server;
        this.port = port;
    }

    public String getName() {
        return listener.getServerData().name;
    }


    @Override
    public void run() {
        startServerListener(server);
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            running = true;
            while (running) {
                ClientConnectionData data = new ClientConnectionData(serverSocket.accept());
                clientList.add(data);
                pool.execute(new ProxyClientListener(data, server, clientList));
            }
        } catch (IOException e) {
            System.err.println("UHHHHH OHHHHH!!!");
        }
    }

    public int getPort() {
        return port;
    }

    public void stop() {
        this.running = false;
    }

    public void openToNewHost() {
        listener = null;
    }
    public boolean isOpenToNewHost() {
        return listener == null;
    }

    public void startServerListener(ConnectionData connectionData) {
        listener = new ProxyServerListener(connectionData, clientList, this);
        new Thread(listener).start();
    }
}
