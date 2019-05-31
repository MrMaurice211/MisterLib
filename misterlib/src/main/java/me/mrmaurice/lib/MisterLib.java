package me.mrmaurice.lib;

import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;

public class MisterLib {

	public static void check() {
		if (instance == null)
			throw new IllegalStateException("Initialize this module with MisterLib.init(JavaPlugin);");
	}
	
	public static void init(JavaPlugin plugin) {
		instance = plugin;
	}

	@Getter
	private static JavaPlugin instance;

}
