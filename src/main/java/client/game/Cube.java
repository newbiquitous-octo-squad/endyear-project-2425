package client.game;

import global.protocol.game.jumpincremental.PlayerData;

import java.awt.*;

public class Cube {
    private PlayerData playerData;

    private int floorHeight = 400;
    private int canvasWidth = 500;

    public Cube(String username) {
        playerData = new PlayerData(username);
    }
    public Cube(PlayerData playerData) {
        this.playerData = playerData;
    }

    public void update() {
        playerData.velocityX += playerData.accelerationX;
        playerData.velocityY += playerData.accelerationY;
        playerData.x += playerData.velocityX;
        playerData.y += playerData.velocityY;

        if (playerData.y + playerData.size > floorHeight) {
            playerData.y = floorHeight - playerData.size;
            playerData.velocityY = 0;
        }

        if (playerData.x + playerData.size > canvasWidth) {
            playerData.x = canvasWidth - playerData.size;
        }

        if (playerData.x < 0) {
            playerData.x = 0;
        }
    }

    public void draw(Graphics g) {
        g.setColor(playerData.color);
        g.fillRect(playerData.x, playerData.y, playerData.size, playerData.size);


        g.setColor(Color.BLACK);
        FontMetrics fm = g.getFontMetrics();
        int textX = playerData.x + (playerData.size - fm.stringWidth(playerData.name)) / 2;
        int textY = playerData.y + (playerData.size - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(playerData.name, textX, textY);
    }

    public void setFromPlayer(PlayerData p) {
        this.playerData = p;
    }

    public void setVelocity(int velocityX, int velocityY) {
        this.playerData.velocityX = velocityX;
        this.playerData.velocityY = velocityY;
    }

    public void setAcceleration(int accelerationX, int accelerationY) {
        this.playerData.accelerationX = accelerationX;
        this.playerData.accelerationY = accelerationY;
    }

    public int getY() {
        return playerData.y;
    }

    public int getX() {
        return playerData.x;
    }

    public int getHeight() {
        return playerData.size;
    }

    public int getFloorHeight() {
        return floorHeight;
    }

    public String getUsername() {
        return playerData.name;
    }

    public PlayerData getPlayerData() {
        return playerData;
    }
}