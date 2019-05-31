package me.mrmaurice.lib.packets.listeners;

import me.mrmaurice.lib.packets.PacketHook;
import me.mrmaurice.lib.utils.Util;

public class TinyListener extends PacketHook {

	public void startListening() {
		Util.info("Using TinyProtocol.");
		// new OnPacket() {
		//
		// @Override
		// public void onPacketInAsync(Player sender, Channel channel, Object packet) {
		//
		// String name = packet.getClass().getSimpleName();
		//
		// if (!name.equals("PacketPlayInUseEntity"))
		// return;
		//
		// int id = f.get(packet);
		//
		// TaskUtil.run(() -> handleUse(sender, id));
		// }
		//
		// });
	}

	@Override
	public void stopListening() {
		// prot.close();
	}

}
