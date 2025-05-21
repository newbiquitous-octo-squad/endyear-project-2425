package server;

import global.AbstractListener;
import global.ConnectionData;

public class ServerListener extends AbstractListener {

    public ServerListener(ConnectionData proxyServer) {
        super(proxyServer);
    }

    @Override
    public void run() {
       while (!isClosed) {
           readMessage().ifPresent(m -> System.out.println("Received a message"));
       }
    }
}
