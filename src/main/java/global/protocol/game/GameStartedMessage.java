package global.protocol.game;

import global.GameType;
import global.protocol.Message;

public class GameStartedMessage extends Message {
    private static final long serialVersionUID = 1L;
    public GameType gameType;
    public GameStartedMessage(GameType gameType) {
        this.gameType = gameType;
    }
}
