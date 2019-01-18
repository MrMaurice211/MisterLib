package me.mrmaurice.lib.events.packets;

import org.bukkit.event.Event;

import me.mrmaurice.lib.utils.Reflections;

public abstract class PacketEvent extends Event {

	private final Object packet;

	public PacketEvent(Object packet) {
		this.packet = packet;
	}

	public <T> T getField(String field) {
		return Reflections.getField(packet.getClass(), field).get(packet);
	}

}
