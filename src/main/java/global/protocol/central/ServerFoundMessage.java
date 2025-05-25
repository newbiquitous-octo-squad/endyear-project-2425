package global.protocol.central;

import global.protocol.Message;

public class ServerFoundMessage extends Message {
    public int port;
    public ServerFoundMessage(int port) {
        this.port = port;
    }
}
