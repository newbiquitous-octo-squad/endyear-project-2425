package global.protocol.game.jumpincremental;

import server.game.jumpincremental.JumpIncremental;

import java.awt.*;
import java.io.Serializable;

public class PlayerData implements Serializable {
    private static final long serialVersionUID = 1L;
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


    public void update() {
        velocityX += accelerationX;
        velocityY += accelerationY;
        x += velocityX;
        y += velocityY;

        if (y + size > JumpIncremental.FLOOR_HEIGHT) {
            y = JumpIncremental.FLOOR_HEIGHT - size;
            velocityY = 0;
        }

        if (x + size > JumpIncremental.CANVAS_WIDTH) {
            x = JumpIncremental.CANVAS_WIDTH - size;
        }

        if (x < 0) {
            x = 0;
        }
    }
}
