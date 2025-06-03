package client.game;

import java.awt.*;

public class Cube {
    private int x, y, size;
    private Color color;
    private int velocityX, velocityY;
    private int accelerationX, accelerationY;

    private String text;

    private int floorHeight = 400;
    private int canvasWidth = 510;

    public Cube(int x, int y, int size, String text, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
        this.velocityX = 3; // Temporary
        this.velocityY = 0;
        this.accelerationX = 0;
        this.accelerationY = 0;
        this.text = text;
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

        // TEMPORARY:
        if (x <= 0 || x + size >= canvasWidth) {
            velocityX = -velocityX;
        }
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, size, size);

        g.setColor(Color.BLACK);
        FontMetrics fm = g.getFontMetrics();
        int textX = x + (size - fm.stringWidth(text)) / 2;
        int textY = y + (size - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(text, textX, textY);
    }

    public void setVelocity(int velocityX, int velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public void setAcceleration(int accelerationX, int accelerationY) {
        this.accelerationX = accelerationX;
        this.accelerationY = accelerationY;
    }
}