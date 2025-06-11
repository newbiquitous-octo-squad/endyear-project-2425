package global.protocol.game.jumpincremental;

import global.protocol.game.GameMessage;

// only in 1 int since i don't want to store like 1000 booleans
public class ClientKeyMessage extends GameMessage {
    public static int KEY_W = 0b1,
            KEY_A = 0b10,
            KEY_D = 0b100;

    public int keys;
    public String username;
    public ClientKeyMessage(String username, int keys) {
        this.username = username;
        this.keys = keys;
    }

    public boolean hasKey(char key) {
        return switch (key) {
            case 'W' -> (keys & KEY_W) != 0;
            case 'A' -> (keys & KEY_A) != 0;
            case 'D' -> (keys & KEY_D) != 0;
            default -> {
                System.out.println("saw an unexpected key: " + key);
                yield false;
            }
        };
    }
}