package me.mrmaurice.lib.events.packets;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PacketSentEvent extends PacketEvent {

	public PacketSentEvent(Object packet, Player target, int id) {
		super(packet);
	}

	@Override
	public HandlerList getHandlers() {
		return null;
	}

}
