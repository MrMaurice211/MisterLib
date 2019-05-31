package me.mrmaurice.lib.packets;

import me.mrmaurice.lib.packets.events.PacketInEvent;
import me.mrmaurice.lib.packets.events.PacketOutEvent;

public abstract class PacketListener {
	
	public void onPacketRecieved(PacketInEvent event) {}
	
	public void onPacketSent(PacketOutEvent event) {}

}
