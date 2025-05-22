package global;

import global.protocol.Message;

import java.io.IOException;
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
            System.out.println("Reading message...");
            Message out = (Message) connectionData.getInput().readObject();
            System.out.println("Read message " + out.getClass());
            return Optional.of(out);
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
