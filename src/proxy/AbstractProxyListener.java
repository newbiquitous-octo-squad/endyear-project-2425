package proxy;

import protocol.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Optional;

public abstract class AbstractProxyListener implements Runnable {
    protected ClientData clientData;
    public AbstractProxyListener(ClientData clientData) {
        this.clientData = clientData;
    }

    public abstract void onDisconnect();

    protected void send(Message m, ClientData receiver) {
        try (ObjectOutputStream out = receiver.getOutput()) {
            out.writeObject(m);
            out.flush();
        } catch (IOException e) {
            System.err.printf("FAILED TO SEND MESSAGE %s\n", m.getClass());
        }
    }

    protected Optional<Message> readMessage() {
        try {
            return Optional.of((Message) clientData.getInput().readObject());
        } catch (ClassNotFoundException e) {
            System.err.println("Invalid class.");
        } catch (IOException e) {
        }
        return Optional.empty();
    }
}
