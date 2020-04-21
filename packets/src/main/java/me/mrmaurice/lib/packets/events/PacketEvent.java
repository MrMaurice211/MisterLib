package me.mrmaurice.lib.packets.events;

import me.mrmaurice.lib.reflections.Reflections;
import org.bukkit.entity.Player;

import lombok.Getter;
import lombok.Setter;

public abstract class PacketEvent {

    protected Player player;

    private Object packet;
    @Setter
    @Getter
    private boolean cancelled;

    public PacketEvent(Object packet, Player player) {
        this.player = player;
        this.packet = packet;
    }

    /**
     * Modify a value of the packet
     *
     * @param field Name of the field to modify
     * @param value Value to be assigned to the field
     */
    public void setPacketValue(String field, Object value) {
        Reflections.getField(packet.getClass(), field).set(packet, value);
    }

    /**
     * Get a value of the packet
     *
     * @param field Name of the field
     * @return current value of the field
     */
    public <T> T getPacketValue(String field) {
        return Reflections.getField(packet.getClass(), field).get(packet);
    }

    /**
     * @return the sent or received packet as an Object
     */
    public Object getPacket() {
        return this.packet;
    }

    /**
     * @return the class name of the sent or received packet
     */
    public String getPacketName() {
        return packet.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return "";
        // return "Packet{ " + (this.getClass().equals(SentPacket.class) ? "[> OUT >]" :
        // "[< IN <]") + " "
        // + this.getPacketName() + " " + (this.hasPlayer() ? this.getPlayername()
        // : this.hasChannel() ? this.getChannel().channel() : "#server#")
        // + " }";
    }

}
