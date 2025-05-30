package global.protocol;

public class ClientJoinMessage extends Message {
    public String username;
    public ClientJoinMessage(String username) {
        this.username = username;
    }

}
