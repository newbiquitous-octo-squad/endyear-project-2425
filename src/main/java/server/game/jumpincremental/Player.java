package server.game.jumpincremental;

import java.io.Serializable;

public class Player implements Serializable {
    public long score = 0;
    public int posX = 0, posY = 0, velX = 0, velY = 0, accX = 0, accY = 0;
}
