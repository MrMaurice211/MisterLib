package me.mrmaurice.lib.packets.events;

import org.bukkit.entity.Player;

public class PacketInEvent extends PacketEvent {

	public PacketInEvent(Object packet, Player player) {
		super(packet, player);
	}
	
	public Player getSource() {
		return player;
	}

}
