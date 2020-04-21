package me.mrmaurice.lib.packets;

import java.util.List;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Lists;

import lombok.Getter;
import me.mrmaurice.lib.packets.events.PacketInEvent;
import me.mrmaurice.lib.packets.events.PacketOutEvent;

public class Packets {

	@Getter
	private static JavaPlugin instance;
	private static List<PacketListener> listeners = Lists.newArrayList();

	public static void init(JavaPlugin plugin) {
		instance = plugin;
	}

	public static void check() {
		if (instance == null)
			throw new IllegalStateException("Initialize this class with Packets.init(JavaPlugin);");
	}

	public static void addListener(PacketListener listener) {
		check();
		listeners.add(listener);
	}

	public static void removeListener(PacketListener listener) {
		check();
		listeners.remove(listener);
	}

	public static void callIncoming(PacketInEvent event) {
		listeners.forEach(l -> l.onPacketRecieved(event));
	}

	public static void callOutgoing(PacketOutEvent event) {
		listeners.forEach(l -> l.onPacketSent(event));
	}

}
