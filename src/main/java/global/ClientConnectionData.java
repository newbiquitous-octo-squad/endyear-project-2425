package global;

import java.io.IOException;
import java.net.Socket;

public class ClientConnectionData extends ConnectionData {
    
    private String name;

    public ClientConnectionData(Socket socket, String name) throws IOException {
        super(socket);
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
