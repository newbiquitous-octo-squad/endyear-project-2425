package global.protocol.central;

import global.protocol.Message;

public class ServerFoundMessage extends Message {
    private static final long serialVersionUID = 1L;
    public int port;
    public ServerFoundMessage(int port) {
        this.port = port;
    }
}
