package proxy;

import global.AbstractListener;
import global.ConnectionData;
import global.protocol.Message;
import global.protocol.central.GetServerRequestMessage;
import global.protocol.central.ServerFoundMessage;
import global.protocol.central.ServerNotFoundMessage;
import global.protocol.central.transfer.ElevateToHostMessage;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerSharer implements Runnable {
    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(Proxy.SHARER_PORT)) {
            while (true) {
                new Thread(new ServerSharerListener(new ConnectionData(serverSocket.accept()))).start();
            }
        } catch (IOException e) {
            System.err.println("bad news: server sharer doesn't share (and thus doesn't care)");
            e.printStackTrace();
        }
    }
}

class ServerSharerListener extends AbstractListener {

    public ServerSharerListener(ConnectionData connectionData) {
        super(connectionData);
    }

    @Override
    public void processMessage(Message message) {
        switch (message) {
            case GetServerRequestMessage serverRequestMessage -> {
                int port = Proxy.getPortByServerName(serverRequestMessage.serverName);
                send(port == -1 ? new ServerNotFoundMessage() : new ServerFoundMessage(port), connectionData);
            }
            case ElevateToHostMessage elevateToHostMessage -> {
                Proxy.giveInstanceNewHost(elevateToHostMessage.serverName, connectionData);
            }
            default -> {}
                // nothing
        }
    }
}