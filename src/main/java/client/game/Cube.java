package client.game;

import java.awt.*;

public class Cube {
    public int x, y, size;
    private Color color;

    public Cube(int x, int y, int size, Color color) {
        this.x = x;
        this.y = y;
        this.size = size;
        this.color = color;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillRect(x, y, size, size);
    }
}