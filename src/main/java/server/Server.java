package server;

import global.ConnectionData;
import global.Sender;
import global.protocol.PingMessage;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Server {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 12345);
        ConnectionData proxyConnectionData = new ConnectionData(socket); // host and port definitions are temporary, obviously
        ServerListener listener = new ServerListener(proxyConnectionData);
        new Thread(listener).start();

        // send a ping whenever the user types anything
        Scanner s = new Scanner(System.in);
        while (true) {
            s.nextLine();
            Sender.send(new PingMessage(), proxyConnectionData);
        }
    }
}
