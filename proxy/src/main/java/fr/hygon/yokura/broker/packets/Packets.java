
package fr.hygon.yokura.broker.packets;

public enum Packets {
    REGISTER_CLIENT_PACKET(0, RegisterClientPacket.class),
    CHANNEL_PACKET(1, ChannelPacket.class),
    MESSAGE_PACKET(2, MessagePacket.class);

    private final int packetId;
    private final Class<? extends Packet> packet;

    Packets(int packetId, Class<? extends Packet> packet) {
        this.packetId = packetId;
        this.packet = packet;
    }

    public int getPacketId() {
        return packetId;
    }

    public Class<? extends Packet> getPacket() {
        return packet;
    }

    /**
     * Gets a packet from its ID.
     * @param id the id of the packet
     * @return null if no packet exists with the specified ID
     */
    public static Packet getPacketById(int id) {
        for (Packets packets : Packets.values()) {
            if (packets.packetId == id) {
                try {
                    return (Packet) packets.getPacket().getConstructors()[0].newInstance();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * Gets the ID of a packet.
     * @param packet the packet
     * @return the ID of the specified packet
     */
    public static int getIdByPacket(Packet packet) {
        for (Packets packets : Packets.values()) {
            if (packets.getPacket().equals(packet.getClass())) {
                return packets.getPacketId();
            }
        }

        return -1;
    }
}
