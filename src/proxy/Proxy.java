package proxy;

import global.ConnectionData;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Proxy {
    public static final int PORT = 12345;
    private static final List<ConnectionData> clientList = Collections.synchronizedList(new ArrayList<>());
    public static void main(String[] args) {
        ExecutorService pool = Executors.newFixedThreadPool(1000);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            ConnectionData server = new ConnectionData(serverSocket.accept());
            pool.execute(new ProxyServerListener(server, clientList));


            // TODO: LET THERE BE PROXY OR SERVER OR WHATEVER!!!

            while (true) {
                ConnectionData data = new ConnectionData(serverSocket.accept());
                clientList.add(data);
                pool.execute(new ProxyClientListener(data, server));
            }
        } catch (IOException e) {
            System.err.println("UHHHHH OHHHHH!!!");
        }
    }
}
