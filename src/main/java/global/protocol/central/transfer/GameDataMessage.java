package global.protocol.central.transfer;

import global.protocol.Message;
import server.game.GameData;

public class GameDataMessage extends Message {
    public GameData gameData;

    public GameDataMessage(GameData gameData) {
        this.gameData = gameData;
    }
}
