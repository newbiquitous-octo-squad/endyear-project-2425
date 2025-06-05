package global.protocol.game;

public class GameUnregisterMessage extends GameMessage {
    public String username;

    public GameUnregisterMessage(String username) {
        this.username = username;
    }
}
