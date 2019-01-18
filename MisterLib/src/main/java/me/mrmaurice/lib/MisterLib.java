package me.mrmaurice.lib;

import java.util.Map;
import java.util.function.Function;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Maps;

import lombok.Getter;
import me.mrmaurice.lib.gui.GUIListener;

public class MisterLib {

	@Getter
	private static Map<Class<?>, Function<Enum<?>, String>> converters = Maps.newHashMap();

	public static void addConverter(Class<?> clazz, Function<Enum<?>, String> func) {
		converters.put(clazz, func);
	}
	
	public static void init(JavaPlugin plugin) {
		new MisterLib(plugin);
	}
	
	private MisterLib(JavaPlugin plugin) {
		MisterLib.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(new GUIListener(), plugin);
	}

	@Getter
	private static JavaPlugin plugin;

}
