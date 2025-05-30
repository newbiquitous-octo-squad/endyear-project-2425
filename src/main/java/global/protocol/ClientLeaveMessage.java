package global.protocol;

public class ClientLeaveMessage extends Message {

    public String username;

    public ClientLeaveMessage(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return username + " has left the server...\n";
    }
}
