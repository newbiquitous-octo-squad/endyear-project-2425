package global;

import java.io.IOException;
import java.net.Socket;

public class ClientConnectionData extends ConnectionData {
    
    private String name;

    public ClientConnectionData(Socket socket, String name) throws IOException {
        super(socket);
        this.name = name;
    }

    public ClientConnectionData(Socket socket) throws IOException {
        this(socket,null);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
