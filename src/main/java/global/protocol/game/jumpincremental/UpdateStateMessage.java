package global.protocol.game.jumpincremental;

import global.protocol.game.GameMessage;
import server.game.jumpincremental.Player;

import java.util.Map;

public class UpdateStateMessage extends GameMessage {
    public Map<String, Player> data;
    public UpdateStateMessage(Map<String, Player> data) {
        this.data = data;
    }
}
