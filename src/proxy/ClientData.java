package proxy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientData {
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;

    public Socket getSocket() {
        return socket;
    }

    public ObjectOutputStream getOutput() {
        return output;
    }

    public ObjectInputStream getInput() {
        return input;
    }

    public ClientData(Socket socket) {
        this.socket = socket;
        try {
            this.input = new ObjectInputStream(socket.getInputStream());
            this.output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.err.println("UH OH");
            ex.printStackTrace();
        }
    }
}
