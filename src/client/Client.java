package client;

import global.ConnectionData;

import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        new Thread(
                new ClientListener(new ConnectionData(new Socket("localhost", 12345))) // obviously, change the host and port later
        ).start();
        // TODO: GUI STUFF AND LIKE LITERALLY EVERYTHING ELSE
    }
}
