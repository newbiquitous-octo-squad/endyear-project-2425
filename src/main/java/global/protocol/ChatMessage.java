package global.protocol;

public class ChatMessage extends Message {
    public String message;
    public String sender;

    public ChatMessage(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    @Override
    public String toString() {
        return sender + ": " + message + "\n";
    }
}
