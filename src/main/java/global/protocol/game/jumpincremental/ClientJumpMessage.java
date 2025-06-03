package global.protocol.game.jumpincremental;

import global.protocol.game.GameMessage;

public class ClientJumpMessage extends GameMessage {
    public String username;

    public ClientJumpMessage(String username) {
        this.username = username;
    }
}
