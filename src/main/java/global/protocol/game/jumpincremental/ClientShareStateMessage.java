package global.protocol.game.jumpincremental;

import global.protocol.game.GameMessage;

public class ClientShareStateMessage extends GameMessage {
    public String name;
    public int posX, posY, velX, velY, accX, accY;

    public ClientShareStateMessage(String name, int posX, int posY, int velX, int velY, int accX, int accY) {
        this.name = name;
        this.posX = posX;
        this.posY = posY;
        this.velX = velX;
        this.velY = velY;
        this.accX = accX;
        this.accY = accY;
    }
}
