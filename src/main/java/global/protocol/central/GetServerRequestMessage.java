package global.protocol.central;

import global.protocol.Message;

public class GetServerRequestMessage extends Message {
    public String serverName;
    public GetServerRequestMessage(String serverName) {
        this.serverName = serverName;
    }
}
