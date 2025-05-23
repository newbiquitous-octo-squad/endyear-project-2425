package server;

import global.ConnectionData;
import global.Sender;
import global.protocol.PingMessage;
import global.protocol.ServerStartupInfoMessage;
import global.ServerData;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Server implements Runnable {
    private ServerData serverData;

    public Server(ServerData serverData) {
        this.serverData = serverData;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket("localhost", 12345); // TODO: SET TO PROXY'S HOST DATA, WHICH SHOULD BE A GLOBAL FIELD ACCESSIBLE ANYWHERE
            ConnectionData proxyConnectionData = new ConnectionData(socket); // host and port definitions are temporary, obviously
            ServerListener listener = new ServerListener(proxyConnectionData);
            new Thread(listener).start();
            Sender.send(new ServerStartupInfoMessage(serverData), proxyConnectionData);

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
