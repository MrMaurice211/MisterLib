package me.mrmaurice.lib.config;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import lombok.Builder;
import me.mrmaurice.lib.MisterLib;
import me.mrmaurice.lib.utils.FileUtil;
import me.mrmaurice.lib.utils.StringUtil;
import me.mrmaurice.lib.utils.Util;

@Builder
public class ConfigUpdater {

	@Builder.Default
	private int newVersion = 1, oldVersion = 1;
	@Builder.Default
	private boolean instantDeprecated = false;
	@Builder.Default
	private String deprecatedName = "old_config";

	private File path;
	private List<String> lines;

	public void checkUpdate() {
		path = new File(MisterLib.getPlugin().getDataFolder(), "config.yml");

		if (oldVersion == 0 || instantDeprecated) {
			if (oldVersion != newVersion)
				deprecateConfig();
			return;
		}

		if (oldVersion == newVersion) {
			Util.info("Your config is updated!");
			return;
		}

		lines = FileUtil.readFile(path);
		updateConfig();
	}

	private void updateConfig() {

		Util.info("Updating the config!");

		List<String> newLines = FileUtil.readInsideFile("/config.yml");

		lines.removeIf(s -> s.trim().isEmpty() || s.trim().startsWith("#") || s.split(":").length == 1);
		lines.forEach(s -> {
			String[] a = s.split(":");
			String newS = StringUtil.joinString(Arrays.copyOfRange(a, 1, a.length), ":");
			int index = StringUtil.getIndex(a[0], newLines);
			if (index > -1)
				newLines.set(index, newLines.get(index).split(":")[0] + ":" + newS);
		});

		newLines.set(StringUtil.getIndex("configVersion", newLines), "configVersion: " + newVersion);
		FileUtil.writeFile(path, newLines);
		Util.info("Your config is now updated!");
		ConfigManager.of("config").reload();
	}

	private void deprecateConfig() {
		Util.info("Now your config is deprecated please check your folder for re-setting it!");
		File old = new File(path.getParentFile(), deprecatedName + ".yml");
		path.renameTo(old);
		ConfigManager.of("config").reload();
	}

}
