package global.protocol.game;

import global.protocol.Message;

public class GameRegisterMessage extends GameMessage {
    private static final long serialVersionUID = 1L;


    public String username;

    public GameRegisterMessage(String username) {
        this.username = username;
    }
}
