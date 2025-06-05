package global.protocol.game.jumpincremental;

import java.awt.*;
import java.io.Serializable;

public class PlayerData implements Serializable {
    public String name;
    public long score = 0;
    public int x, y, size, velocityX, velocityY, accelerationX, accelerationY = 1;
    public Color color;

    public PlayerData(String username) {
        this.color = new Color((int) (Math.random()*205 + 50), (int) (Math.random()*205 + 50), (int) (Math.random()*205 + 50));
        x = 100;
        y = 100;
        size = 50;
        name = username;
    }
}
