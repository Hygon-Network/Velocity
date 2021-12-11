
package fr.hygon.yokura.broker;

import fr.hygon.yokura.broker.handlers.PubSubManager;

import java.util.ArrayList;

public abstract class BrokerPubSub {
    public ArrayList<String> channels = new ArrayList<>();

    public void onReceive(String channel, byte[] message) {
    }

    public void registerChannel(String channel) {
        channels.add(channel);
        PubSubManager.registerChannel(this, channel);
    }

    public void unregisterChannel(String channel) {
        channels.remove(channel);
        PubSubManager.unregisterChannel(this, channel);
    }
}
