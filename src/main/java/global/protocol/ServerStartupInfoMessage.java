package global.protocol;

import global.ServerData;

public class ServerStartupInfoMessage extends Message {
    private static final long serialVersionUID = 1L;
    public ServerData serverData;
    public ServerStartupInfoMessage(ServerData serverData) {
        this.serverData = serverData;
    }
}
