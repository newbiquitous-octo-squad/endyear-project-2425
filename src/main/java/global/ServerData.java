package global;

import java.io.Serializable;

public class ServerData implements Serializable {
    private static final long serialVersionUID = 1L;
    public String name;
    public ServerData() {
    }

    public ServerData(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
}