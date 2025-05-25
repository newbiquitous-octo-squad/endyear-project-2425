package client;

import global.AbstractListener;
import global.ConnectionData;
import global.protocol.Message;
import global.protocol.PingMessage;
import global.protocol.PongMessage;

public class ClientListener extends AbstractListener {
    public ClientListener(ConnectionData connectionData) {
        super(connectionData);
    }

    @Override
    public void processMessage(Message message) {
        if (message instanceof PingMessage) {
            System.out.println("Received Ping message");
            send(new PongMessage(), connectionData);
        }
    }
}
