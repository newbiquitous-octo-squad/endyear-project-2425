package client.game;

import server.game.jumpincremental.Player;

import java.awt.*;

public class Cube {
    private int x, y, size;
    private Color color;
    public int velocityX, velocityY;
    public int accelerationX, accelerationY;

    private String username;

    private int floorHeight = 400;
    private int canvasWidth = 500;

    public Cube(int x, int y, int size, String username, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
        this.velocityX = 0;
        this.velocityY = 0;
        this.accelerationX = 0;
        this.accelerationY = 0;
        this.username = username;
    }

    public void update() {
        velocityX += accelerationX;
        velocityY += accelerationY;
        x += velocityX;
        y += velocityY;

        if (y + size > floorHeight) {
            y = floorHeight - size;
            velocityY = 0;
        }

        if (x + size > canvasWidth) {
            x = canvasWidth - size;
        }

        if (x < 0) {
            x = 0;
        }
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, size, size);

        g.setColor(Color.BLACK);
        FontMetrics fm = g.getFontMetrics();
        int textX = x + (size - fm.stringWidth(username)) / 2;
        int textY = y + (size - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(username, textX, textY);
    }

    public void setFromPlayer(Player p) {
        System.out.println(velocityX);
        System.out.println(p.velX);
        x = p.posX;
        y = p.posY;
        velocityX = p.velX;
        velocityY = p.velY;
        accelerationX = p.accX;
        accelerationY = p.accY;
    }

    public void setVelocity(int velocityX, int velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void setAcceleration(int accelerationX, int accelerationY) {
        this.accelerationX = accelerationX;
        this.accelerationY = accelerationY;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int getHeight() {
        return size;
    }

    public int getFloorHeight() {
        return floorHeight;
    }

    public String getUsername() {
        return username;
    }
}