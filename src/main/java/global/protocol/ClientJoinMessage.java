package global.protocol;

public class ClientJoinMessage extends Message {
    private static final long serialVersionUID = 1L;
    public String username;
    public ClientJoinMessage(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "Welcome " + username + "!\n";
    }
}
