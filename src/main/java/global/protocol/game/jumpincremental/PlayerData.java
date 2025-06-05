package global.protocol.game.jumpincremental;

import java.io.Serializable;

public class PlayerData implements Serializable {
    public String name;
    public long score = 0;
    public int x, y, size, velocityX, velocityY, accelerationX, accelerationY = 1;
}
