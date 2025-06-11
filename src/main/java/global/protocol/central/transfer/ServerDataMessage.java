package global.protocol.central.transfer;

import global.ServerData;
import global.protocol.Message;

public class ServerDataMessage extends Message {
    public ServerData serverData;

    public ServerDataMessage(ServerData serverData) {
        this.serverData = serverData;
    }
}
