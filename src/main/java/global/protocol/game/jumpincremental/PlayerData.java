package global.protocol.game.jumpincremental;

import server.game.jumpincremental.JumpIncremental;

import java.awt.*;
import java.io.Serializable;

public class PlayerData implements Serializable {
    private static final long serialVersionUID = 1L;
    public String name;
    public long score = 0;
    public float x, y, size, velocityX, velocityY = 1;
    public int accelerationX, accelerationY = 1;
    public Color color;

    public PlayerData(String username) {
        this.color = new Color((int) (Math.random()*205 + 50), (int) (Math.random()*205 + 50), (int) (Math.random()*205 + 50));
        x = 100;
        y = 100;
        size = 50;
        name = username;
    }
    public float lerpX(float targetX, float alpha) {
        this.x += (targetX - this.x) * alpha;
        return this.x;
    }

    public float lerpY(float targetY, float alpha) {
        this.y += (targetY - this.y) * alpha;
        return this.y;
    }


    public void update() {
        velocityX += accelerationX;
        velocityY += accelerationY;
        x = (int) lerpX(x + velocityX, 0.1f);
        y = (int) lerpY(y + velocityY, 0.1f);

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

    public PlayerData copy() {
        PlayerData out = new PlayerData(this.name);
        out.x = this.x;
        out.y = this.y;
        out.velocityX = this.velocityX;
        out.velocityY = this.velocityY;
        out.score = this.score;
        out.color = this.color;
        return out;
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "name='" + name + '\'' +
                ", x=" + x +
                ", y=" + y +
                ", score=" + score +
                ", velocityX=" + velocityX +
                ", velocityY=" + velocityY +
                ", color=" + color +
                '}';
    }
}
