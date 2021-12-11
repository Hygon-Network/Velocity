package fr.hygon.yokura.broker.packets;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;

public interface Packet {
    void read(ChannelHandlerContext ctx, ByteBuf in);

    void write(ByteBuf out);
}
