package server;

import global.ConnectionData;
import global.Sender;
import global.protocol.PingMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Server implements Runnable {
    private int port;
    private String name;

    public String getName() {
        return name;
    }

    public Server(int port, String name) {
        this.port = port;
        this.name = name;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", port);
            ConnectionData proxyConnectionData = new ConnectionData(socket); // host and port definitions are temporary, obviously
            ServerListener listener = new ServerListener(proxyConnectionData);
            new Thread(listener).start();

            // send a ping whenever the user types anything
            Scanner s = new Scanner(System.in);
            while (true) {
                s.nextLine();
                Sender.send(new PingMessage(), proxyConnectionData);
            }
        } catch (IOException e) {
            System.err.println("IOException caught trying to server");
            e.printStackTrace();
        }
    }
}
