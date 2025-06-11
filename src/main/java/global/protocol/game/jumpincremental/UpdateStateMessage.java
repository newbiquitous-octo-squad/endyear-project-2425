package global.protocol.game.jumpincremental;

import global.protocol.game.GameMessage;

import java.util.List;

public class UpdateStateMessage extends GameMessage {
    private static final long serialVersionUID = 1L;

    public PlayerData[] data;
    public UpdateStateMessage(List<PlayerData> data) {
        this.data = data.toArray(new PlayerData[0]);
    }
}
