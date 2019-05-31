package me.mrmaurice.lib.packets.listeners;

import org.inventivetalent.packetlistener.PacketListenerAPI;
import org.inventivetalent.packetlistener.handler.PacketHandler;
import org.inventivetalent.packetlistener.handler.ReceivedPacket;
import org.inventivetalent.packetlistener.handler.SentPacket;

import me.mrmaurice.lib.packets.PacketHook;
import me.mrmaurice.lib.packets.Packets;
import me.mrmaurice.lib.packets.events.PacketInEvent;
import me.mrmaurice.lib.packets.events.PacketOutEvent;
import me.mrmaurice.lib.utils.Util;

public class PacketAPIListener extends PacketHook {

	private PacketHandler handler;

	public void startListening() {
		Util.info("Using PacketListenerAPI.");
		PacketListenerAPI.addPacketHandler(handler = new PacketHandler(Packets.getInstance()) {

			@Override
			public void onReceive(ReceivedPacket packet) {
				PacketInEvent event = new PacketInEvent(packet.getPacket(), packet.getPlayer());
				Packets.callIncoming(event);
				packet.setCancelled(event.isCancelled());
			}

			@Override
			public void onSend(SentPacket packet) {
				PacketOutEvent event = new PacketOutEvent(packet.getPacket(), packet.getPlayer());
				Packets.callOutgoing(event);
				packet.setCancelled(event.isCancelled());
			}

		});
	}

	@Override
	public void stopListening() {
		PacketListenerAPI.removePacketHandler(handler);
	}

}
