
package fr.hygon.yokura.broker.handlers;

import fr.hygon.yokura.broker.BrokerPubSub;
import fr.hygon.yokura.broker.Message;
import fr.hygon.yokura.broker.packets.ChannelPacket;
import fr.hygon.yokura.broker.packets.MessagePacket;

import java.util.ArrayList;

public class PubSubManager {
    private static final ArrayList<BrokerPubSub> pubSubs = new ArrayList<>();

    /**
     * Registers a PubSub runnable to a channel.
     * @param pubSub the runnable
     * @param channel the channel
     */
    public static void registerChannel(BrokerPubSub pubSub, String channel) {
        if (!pubSubs.contains(pubSub)) {
            pubSubs.add(pubSub);
        }

        ChannelPacket channelPacket = new ChannelPacket(ChannelPacket.Actions.REGISTER, channel);
        Broker.sendPacket(channelPacket);
    }

    /**
     * Unregisters a PubSub runnable from a channel.
     * @param pubSub the runnable
     * @param channel the channel
     */
    public static void unregisterChannel(BrokerPubSub pubSub, String channel) {
        if (pubSub.channels.isEmpty()) {
            pubSubs.remove(pubSub);
        }

        boolean isClientStillUsingChannel = false; // Other pubsubs might still use it!
        for (BrokerPubSub brokerPubSubs : pubSubs) {
            if (brokerPubSubs.channels.contains(channel)) {
                isClientStillUsingChannel = true;
                break;
            }
        }

        if (!isClientStillUsingChannel) { // No pubsubs are listening for this channel
            // so we can unregister it
            ChannelPacket channelPacket = new ChannelPacket(ChannelPacket.Actions.UNREGISTER, channel);
            Broker.sendPacket(channelPacket);
        }
    }

    /**
     * Sends a message to the broker.
     * @param message the message to be published
     */
    public static void publish(Message message) {
        MessagePacket messagePacket = new MessagePacket(message);
        Broker.sendPacket(messagePacket);
    }

    /**
     * Handles a message and execute all the concerned PubSub runnables.
     * @param channel the channel the message was received on
     * @param message the message content
     */
    public static void handleMessage(String channel, byte[] message) {
        for (BrokerPubSub brokerPubSub : pubSubs) {
            if (brokerPubSub.channels.contains(channel)) {
                brokerPubSub.onReceive(channel, message);
            }
        }
    }
}
