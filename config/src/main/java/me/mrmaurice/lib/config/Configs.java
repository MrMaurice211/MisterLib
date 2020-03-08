package me.mrmaurice.lib.config;

import java.util.Map;
import java.util.function.Function;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Configs {

	@Getter
	private static String prefix;

	@Getter
	private static Map<Class<?>, Function<Enum<?>, String>> converters = Maps.newHashMap();

	public static void check() {
		if (instance == null)
			throw new IllegalStateException("Initialize this module with Configs.init(JavaPlugin);");
	}

	public static void addConverter(Class<?> clazz, Function<Enum<?>, String> func) {
		converters.put(clazz, func);
	}

	public static void init(JavaPlugin plugin) {
		instance = plugin;
	}

	public static void setPrefix(String prefix) {
		Configs.prefix = prefix;
	}

	@Getter
	private static JavaPlugin instance;

}
