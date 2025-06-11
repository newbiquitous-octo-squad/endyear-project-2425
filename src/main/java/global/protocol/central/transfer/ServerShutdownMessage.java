package global.protocol.central.transfer;

import global.ServerData;
import global.protocol.Message;

public class ServerShutdownMessage extends Message {
    public ServerData serverData;

    public ServerShutdownMessage(ServerData serverData) {
        this.serverData = serverData;
    }
}
