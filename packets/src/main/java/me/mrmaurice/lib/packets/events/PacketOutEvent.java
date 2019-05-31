package me.mrmaurice.lib.packets.events;

import org.bukkit.entity.Player;

public class PacketOutEvent extends PacketEvent {

	public PacketOutEvent(Object packet, Player player) {
		super(packet, player);
	}
	
	public Player getTarget() {
		return player;
	}

}
