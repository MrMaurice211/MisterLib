package me.mrmaurice.lib.gui;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Guis {

	@Getter
	private static JavaPlugin instance;
	@Getter
	private static String textGUI_prefix;

	public static void check() {
		if (instance == null)
			throw new IllegalStateException("Initialize this module with Guis.init(JavaPlugin);");
	}

	public static void init(JavaPlugin plugin) {
		instance = plugin;
		Bukkit.getPluginManager().registerEvents(new GUIListener(), plugin);
	}

}
