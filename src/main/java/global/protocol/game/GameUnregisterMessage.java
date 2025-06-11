package global.protocol.game;

public class GameUnregisterMessage extends GameMessage {
    private static final long serialVersionUID = 1L;

    public String username;

    public GameUnregisterMessage(String username) {
        this.username = username;
    }
}
