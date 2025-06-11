package global.protocol.central.transfer;

import global.protocol.Message;

public class NewHostMessage extends Message {
    public String username;

    public NewHostMessage(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return username + " is now the host.\n";
    }
}
