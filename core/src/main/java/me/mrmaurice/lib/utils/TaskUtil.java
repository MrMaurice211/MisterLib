package me.mrmaurice.lib.utils;

import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import lombok.experimental.UtilityClass;
import me.mrmaurice.lib.MisterLib;

@UtilityClass
public class TaskUtil {

	private static BukkitScheduler scheduler = MisterLib.getInstance().getServer().getScheduler();

	public static BukkitTask runAsync(Runnable run) {
		return runAsyncLater(run, 0);
	}

	public static BukkitTask runAsyncLater(Runnable run, int delay) {
		return runAsyncTimer(run, delay, -1L);
	}

	public static BukkitTask runAsyncTimer(Runnable run, int delay, long period) {
		MisterLib.check();
		return scheduler.runTaskTimerAsynchronously(MisterLib.getInstance(), run, delay, period);
	}

	public static BukkitTask run(Runnable run) {
		return runLater(run, 0);
	}

	public static BukkitTask runLater(Runnable run, long delay) {
		return runTimer(run, delay, -1L);
	}

	public static BukkitTask runTimer(Runnable run, long delay, long period) {
		return scheduler.runTaskTimer(MisterLib.getInstance(), run, delay, period);
	}

}
