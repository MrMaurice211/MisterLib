package me.mrmaurice.lib.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.util.List;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.common.collect.Lists;

import lombok.Getter;
import me.mrmaurice.lib.MisterLib;
import me.mrmaurice.lib.utils.Util;

public class ConfigManager {

	private static File dataFolder = MisterLib.getPlugin().getDataFolder();
	private static List<Config> configs = Lists.newArrayList();

	public static Config of(String name) {
		Config cfg = configs.stream().filter(c -> c.getConfigName().equals(name + ".yml")).findFirst().orElse(null);
		if (cfg != null)
			return cfg;
		cfg = new Config(name);
		configs.add(cfg.reload());
		return cfg;
	}

	public static class Config {

		@Getter
		private String configName;
		private File configFile;
		private YamlConfiguration config;

		public Config(String name) {
			configName = name + ".yml";
			configFile = new File(dataFolder, configName);
		}

		public Config save() {
			try {
				config.save(configFile);
			} catch (Exception e) {
				Util.error("Error saving the config file.");
			}
			return this;
		}

		public Configuration get() {
			if (config == null)
				reload();

			return config;
		}

		public Config reload() {

			try (InputStream is = getClass().getResourceAsStream("/" + configName)) {
				if (!configFile.exists()) {
					Files.copy(is, configFile.toPath(), new CopyOption[0]);
				}
				config = YamlConfiguration.loadConfiguration(configFile);
			} catch (IOException e) {
				Util.error("Error reloading the config");
				e.printStackTrace();
			}
			return this;
		}

		public Config set(String key, Object value) {
			get().set(key, value);
			return this;
		}
		
		public <T, E extends Enum<E>> T get(Enum<E> key, T def) {
			String path = MisterLib.getConverters().get(key.getClass()).apply(key);
			return get(path, def);
		}

		@SuppressWarnings("unchecked")
		public <T> T get(String key, T def) {
			if (key == null || key.isEmpty())
				return def;

			Object result = get().get(key);

			if (result == null)
				return def;

			return (T) result;
		}

	}

}
