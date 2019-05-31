package me.mrmaurice.lib.packets.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;

import me.mrmaurice.lib.packets.Packets;
import me.mrmaurice.lib.packets.PacketHook;
import me.mrmaurice.lib.packets.events.PacketInEvent;
import me.mrmaurice.lib.packets.events.PacketOutEvent;
import me.mrmaurice.lib.utils.Util;

public class ProtocolListener extends PacketHook {

	public void startListening() {
		Util.info("Using ProtocolLib.");
		ProtocolLibrary.getProtocolManager()
				.addPacketListener(new PacketAdapter(Packets.getInstance(), PacketType.values()) {
					@Override
					public void onPacketReceiving(PacketEvent packet) {
						PacketInEvent event = new PacketInEvent(packet.getPacket().getHandle(), packet.getPlayer());
						Packets.callIncoming(event);
						packet.setCancelled(event.isCancelled());
					}

					@Override
					public void onPacketSending(PacketEvent packet) {
						PacketOutEvent event = new PacketOutEvent(packet.getPacket().getHandle(), packet.getPlayer());
						Packets.callOutgoing(event);
						packet.setCancelled(event.isCancelled());
					}
				});
	}

	@Override
	public void stopListening() {
		ProtocolLibrary.getProtocolManager().removePacketListeners(Packets.getInstance());
	}

}
