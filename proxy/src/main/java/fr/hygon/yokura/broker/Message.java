
package fr.hygon.yokura.broker;

public class Message {
    private final String channel;
    private final byte[] message;

    public Message(String channel, byte[] message) {
        this.channel = channel;
        this.message = message.clone();
    }

    public String getChannel() {
        return channel;
    }

    public byte[] getMessage() {
        return message.clone();
    }
}
