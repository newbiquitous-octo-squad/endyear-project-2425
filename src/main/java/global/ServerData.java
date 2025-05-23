package global;

import java.io.Serializable;

public class ServerData implements Serializable {
    public String name;
    public ServerData(String name) {
        this.name = name;
    }
}