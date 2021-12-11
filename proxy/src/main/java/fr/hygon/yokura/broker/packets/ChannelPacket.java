
package fr.hygon.yokura.broker.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

import java.nio.charset.StandardCharsets;

public class ChannelPacket implements Packet {
    private final Actions action;
    private final String channel;

    public ChannelPacket(Actions action, String channel) {
        this.action = action;
        this.channel = channel;
    }

    @Override
    public void read(ChannelHandlerContext ctx, ByteBuf in) { // The client won't receive this packet

    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(action.getActionId());

        out.writeInt(channel.length());
        out.writeCharSequence(channel, StandardCharsets.UTF_8);
    }

    public enum Actions {
        REGISTER(0),
        UNREGISTER(1);

        private final int actionId;

        Actions(int actionId) {
            this.actionId = actionId;
        }

        public int getActionId() {
            return actionId;
        }
    }
}