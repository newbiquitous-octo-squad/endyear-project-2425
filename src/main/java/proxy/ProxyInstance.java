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
    private volatile ServerData serverData;

    public ProxyInstance(ConnectionData server, int port) {
        this.server = server;
        this.port = port;
    }

    public String getName() {
        return serverData.name;
    }


    @Override
    public void run() {
        new Thread(new ProxyServerListener(server, clientList, serverData)).start();
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                ClientConnectionData data = new ClientConnectionData(serverSocket.accept());
                clientList.add(data);
                pool.execute(new ProxyClientListener(data, server, clientList));
            }
        } catch (IOException e) {
            System.err.println("UHHHHH OHHHHH!!!");
        }
    }
}
