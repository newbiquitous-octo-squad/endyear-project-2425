package global.protocol.central.transfer;

import global.ServerData;
import global.protocol.Message;

// can't think of a better name; for the host to get data
public class HostDataMessage extends Message {
    public ServerData serverData;

    public HostDataMessage(ServerData serverData) {
        this.serverData = serverData;
    }
}
