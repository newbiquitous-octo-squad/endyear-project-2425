package proxy;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Proxy {
    public static final int PORT = 12345;
    private static final List<ClientData> clientList = Collections.synchronizedList(new ArrayList<>());
    public static void main(String[] args) throws IOException {
        ExecutorService pool = Executors.newFixedThreadPool(1000);
        ServerSocket serverSocket = new ServerSocket(PORT);

        ClientData server = new ClientData(serverSocket.accept());
        pool.execute(new ProxyServerListener(server, clientList));

        // TODO: LET THERE BE PROXY OR SERVER OR WHATEVER!!!

        while (true) {
            ClientData data = new ClientData(serverSocket.accept());
            clientList.add(data);
            pool.execute(new ProxyClientListener(data, server));
        }
    }
}
