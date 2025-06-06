package global.protocol.central;

import global.protocol.Message;

public class GetServerRequestMessage extends Message {
    private static final long serialVersionUID = 1L;
    public String serverName;
    public GetServerRequestMessage(String serverName) {
        this.serverName = serverName;
    }
}
