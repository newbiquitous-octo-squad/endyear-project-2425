package global;

import server.Server;
import server.game.GameData;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ServerData implements Serializable {
    private static final long serialVersionUID = 1L;
    public String name;
    private transient List<String> chatList = new ArrayList<>();
    public String[] chat;
    public GameData gameData;
    private transient Server server;
    public ServerData() {}

    public ServerData(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void addChatMessage(String c) {
        chatList.add(c);
    }
    
    public void prepareForSending() {
        chat = chatList.toArray(new String[0]);
        gameData = server.getSelectedGame().getGameData();
    }
}