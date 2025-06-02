package global.protocol.game;

import global.protocol.Message;

public class GameRegisterMessage extends Message {

    public String username;

    public GameRegisterMessage(String username) {
        this.username = username;
    }
}
