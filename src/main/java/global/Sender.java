package global;

import global.protocol.Message;

import java.io.IOException;
import java.io.ObjectOutputStream;

// This code is needed like all the time since sending things happens everywhere.
// Solution: put it as a static method in some interface
// Could be a class; literally doesn't matter
public interface Sender {
    static void send(Message m, ConnectionData receiver) {
        ObjectOutputStream out = receiver.getOutput();
        try {
            System.out.println("Sending message " + m.getClass());
            out.writeObject(m);
            out.flush();
        } catch (IOException e) {
            System.err.printf("FAILED TO SEND MESSAGE %s\n", m.getClass());
            System.out.println(e.getMessage());
        }
    }
}
