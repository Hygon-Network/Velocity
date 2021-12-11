
package fr.hygon.yokura.broker.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public class RegisterClientPacket implements Packet {
    @Override
    public void read(ChannelHandlerContext ctx, ByteBuf in) { // The client won't receive this packet

    }

    @Override
    public void write(ByteBuf out) {
        out.writeInt(Packets.REGISTER_CLIENT_PACKET.getPacketId());
        // The UUID has already been wrote before, so we don't need to rewrite it.
    }
}
