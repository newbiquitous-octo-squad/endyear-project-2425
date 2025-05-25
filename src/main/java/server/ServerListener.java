package server;

import global.AbstractListener;
import global.ConnectionData;
import global.protocol.Message;

public class ServerListener extends AbstractListener {

    public ServerListener(ConnectionData proxyServer) {
        super(proxyServer);
    }

    @Override
    public void processMessage(Message message) {
        System.out.println("recieved a message");
    }
}
