package global.protocol.central;

import global.protocol.Message;

public class ConnectionDeclinedMessage extends Message {
    private static final long serialVersionUID = 1L;
    public String reason;

    public ConnectionDeclinedMessage(String reason) {
        this.reason = reason;
    }

}
