package proxy;

import global.ConnectionData;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Proxy {
    private static final List<ProxyInstance> serverList = Collections.synchronizedList(new ArrayList<>());
    ExecutorService pool = Executors.newFixedThreadPool(1000);
    public static final String HOST = "localhost";
    public static final int PORT = 12345;
    public static final int SHARER_PORT = 12346;

    public static void main(String[] args) {
        int port = 25000;
        new Thread(new ServerSharer()).start();
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                ConnectionData server = new ConnectionData(serverSocket.accept());
                while (!isPortAvailable(port))
                    port++;
                ProxyInstance proxyInstance = new ProxyInstance(server, port);
                serverList.add(proxyInstance);
                new Thread(proxyInstance).start();
            }

        } catch (IOException e) {
            System.err.println("Uh oh! Proxy died!");
            e.printStackTrace();
        }
    }

    private static boolean isPortAvailable(int port) {
        try (ServerSocket ignored = new ServerSocket(port)) {
            return true;
        } catch (IOException alsoIgnored) {}
        return false;
    }

    public static int getPortByServerName(String serverName) {
        Optional<ProxyInstance> instance = serverList.stream().filter((i) -> i.getName().equals(serverName)).findFirst();
        return instance.map(ProxyInstance::getPort).orElse(-1);
    }
}
