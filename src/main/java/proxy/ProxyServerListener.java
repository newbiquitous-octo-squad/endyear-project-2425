package proxy;

import global.ClientConnectionData;
import global.ConnectionData;
import global.ServerData;
import global.protocol.ChatMessage;
import global.protocol.Message;
import global.protocol.ServerStartupInfoMessage;
import global.protocol.central.transfer.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ProxyServerListener extends AbstractProxyListener {
    private ServerData serverData;
    private ProxyInstance instance;
    public ProxyServerListener(ConnectionData connectionData, List<ClientConnectionData> clientList, ProxyInstance instance) {
        super(connectionData, clientList);
        this.serverData = new ServerData();
        this.instance = instance;
    }

    public ServerData getServerData() {
        return serverData;
    }

    public void broadcast(Message message) {
        for (ConnectionData client : clientList) {
            send(message, client);
        }
    }

    @Override
    public void processMessage(Message message) {
        switch (message) {
            case ServerStartupInfoMessage serverStartupInfoMessage -> {
                serverData.setName(serverStartupInfoMessage.serverData.name);
            }
            case ChatMessage chatMessage -> {
                System.out.println(clientList.size());
                System.out.println("Broadcasting message from " + chatMessage.sender);
                broadcast(chatMessage);
            }
            case ServerShutdownMessage shutdownMessage -> {
                System.out.println("Initiating transfer process.");
                if (clientList.size() == 1) {
                    System.out.println("Nevermind everyone's gone");
                    instance.stop();
                } else {
                    ClientConnectionData client = clientList.get(1); // we get the 1st element rather than the 0th element since it's most likely that element #0 is our preexisting server
                    System.out.println("Our new host will be " + client.getName());
                    send(new HostDataMessage(shutdownMessage.serverData), client);
                    broadcast(new NewHostMessage(client.getName()));
                    instance.openToNewHost();
                    this.close();
                }
            }
            default -> broadcast(message);
        }
    }

    @Override
    public void onDisconnect() {
        // TODO: HANDLE SUDDEN DISCONNECT
        instance.stop();
    }
}
