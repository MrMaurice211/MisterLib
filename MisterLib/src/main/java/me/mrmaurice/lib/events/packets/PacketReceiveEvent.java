package me.mrmaurice.lib.events.packets;

import org.bukkit.event.HandlerList;

public class PacketReceiveEvent extends PacketEvent {

	public PacketReceiveEvent(Object packet) {
		super(packet);
		// TODO Auto-generated constructor stub
	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
