package global.protocol.game;

import global.GameType;
import global.protocol.Message;

public class GameStartedMessage extends Message {
    public GameType gameType;
    public GameStartedMessage(GameType gameType) {
        this.gameType = gameType;
    }
}
