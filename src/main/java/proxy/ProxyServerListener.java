package proxy;

import global.ClientConnectionData;
import global.ConnectionData;
import global.ServerData;
import global.protocol.Message;
import global.protocol.ServerStartupInfoMessage;

import java.util.List;

public class ProxyServerListener extends AbstractProxyListener {
    ServerData serverData;
    public ProxyServerListener(ConnectionData connectionData, List<ClientConnectionData> clientList) {
        super(connectionData, clientList);
        this.serverData = serverData;
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
            case ServerStartupInfoMessage serverStartupInfoMessage:
                System.out.println(serverStartupInfoMessage.serverData.name);
                System.out.println("Titan toiletmaster has returned for revenge");
                serverData.setName(serverStartupInfoMessage.serverData.name);
            default:
                broadcast(message);
        }
    }

    @Override
    public void onDisconnect() {
        // TODO: TRANSFER SERVER
    }
}
