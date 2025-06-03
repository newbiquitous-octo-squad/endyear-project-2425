package global.protocol.central;

import global.protocol.Message;

public class ConnectionDeclinedMessage extends Message {

    public String reason;

    public ConnectionDeclinedMessage(String reason) {

        this.reason = reason;
    }

}
