package server.game.jumpincremental;

import global.protocol.game.jumpincremental.PlayerData;
import server.game.GameData;

import java.util.List;

public class JumpIncrementalData extends GameData {
    PlayerData[] playerData;
    public JumpIncrementalData(List<PlayerData> data) {
        playerData = data.toArray(new PlayerData[0]);
    }
}
