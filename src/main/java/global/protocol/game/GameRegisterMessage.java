package global.protocol.game;

import global.protocol.Message;

public class GameRegisterMessage extends GameMessage {

    public String username;

    public GameRegisterMessage(String username) {
        this.username = username;
    }
}
