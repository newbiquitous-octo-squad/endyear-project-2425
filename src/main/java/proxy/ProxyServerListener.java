package proxy;

import global.ConnectionData;
import global.ServerData;
import global.protocol.Message;
import global.protocol.ServerStartupInfoMessage;

import java.util.List;

public class ProxyServerListener extends AbstractProxyListener {
    ServerData serverData;
    public ProxyServerListener(ConnectionData connectionData, List<ConnectionData> clientList, ServerData serverData) {
        super(connectionData, clientList);
        this.serverData = serverData;
    }

    public void broadcast(Message message) {
        for (ConnectionData client : clientList) {
            send(message, client);
        }
    }

    public void processMessage(Message message) {
        switch (message) {
            case ServerStartupInfoMessage serverStartupInfoMessage:
                serverData = serverStartupInfoMessage.serverData;
            default:
                broadcast(message);
        }
    }

    @Override
    public void run() {
        while (!isClosed) {
            readMessage().ifPresent(this::processMessage);
        }
    }

    @Override
    public void onDisconnect() {
        // TODO: TRANSFER SERVER
    }
}
