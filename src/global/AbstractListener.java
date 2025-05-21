package global;

import global.protocol.Message;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Optional;

public abstract class AbstractListener implements Runnable {
    protected ConnectionData connectionData;
    protected boolean isClosed = false;
    public AbstractListener(ConnectionData connectionData) {
        this.connectionData = connectionData;
    }

    protected void send(Message m, ConnectionData receiver) {
        Sender.send(m, receiver);
    }

    protected Optional<Message> readMessage() {
        try {
            return Optional.of((Message) connectionData.getInput().readObject());
        } catch (ClassNotFoundException e) {
            System.err.println("Invalid class.");
        } catch (IOException e) {
            System.err.println("Error Reading Message");
            e.printStackTrace();
            close();
        }
        return Optional.empty();
    }

    public void close() {
        isClosed = true;
    }
}
