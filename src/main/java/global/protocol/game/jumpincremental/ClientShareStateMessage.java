package global.protocol.game.jumpincremental;

import global.protocol.game.GameMessage;

public class ClientShareStateMessage extends GameMessage {
    private static final long serialVersionUID = 1L;

    public String name;
    public PlayerData playerData;

    public ClientShareStateMessage(String name, PlayerData playerData) {
        this.name = name;
        this.playerData = playerData;
    }
}
