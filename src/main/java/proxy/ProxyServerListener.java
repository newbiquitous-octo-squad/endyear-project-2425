package proxy;

import global.ClientConnectionData;
import global.ConnectionData;
import global.Sender;
import global.ServerData;
import global.protocol.ChatMessage;
import global.protocol.ClientJoinMessage;
import global.protocol.Message;
import global.protocol.ServerStartupInfoMessage;
import global.protocol.game.jumpincremental.UpdateStateMessage;

import java.util.Arrays;
import java.util.List;

public class ProxyServerListener extends AbstractProxyListener {
    ServerData serverData;
    public ProxyServerListener(ConnectionData connectionData, List<ClientConnectionData> clientList) {
        super(connectionData, clientList);
        this.serverData = new ServerData();
    }

    public ServerData getServerData() {
        return serverData;
    }

    public void broadcast(Message message) {
//        System.out.println("Broadcasti(tan toilet master is always watchi)ng: " + message.getClass().getSimpleName());
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
            case UpdateStateMessage updateStateMessage -> {
                System.out.println(Arrays.toString(updateStateMessage.data));
                broadcast(message);
            }
            default -> broadcast(message);
        }
    }

    @Override
    public void onDisconnect() {
        // TODO: TRANSFER SERVER
    }
}
