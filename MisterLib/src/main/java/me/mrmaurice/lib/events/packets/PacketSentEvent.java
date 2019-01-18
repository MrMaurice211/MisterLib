package me.mrmaurice.lib.events.packets;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PacketSentEvent extends PacketEvent {

	public PacketSentEvent(Object packet, Player target, int id) {
		super(packet);
	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
	    return handlers;
	}

	public static HandlerList getHandlerList() {
	    return handlers;
	}
}
