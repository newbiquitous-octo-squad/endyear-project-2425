package global.protocol;

import global.ServerData;

public class ServerStartupInfoMessage extends Message {
    public ServerData serverData;
    public ServerStartupInfoMessage(ServerData serverData) {
        this.serverData = serverData;
    }
}
