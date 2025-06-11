package global.protocol.central.transfer;

import global.protocol.Message;

public class ElevateToHostMessage extends Message {
    public String serverName;

    public ElevateToHostMessage(String serverName) {
        this.serverName = serverName;
    }
}
