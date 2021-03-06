package me.mrmaurice.lib.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import me.mrmaurice.lib.MisterLib;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

import net.md_5.bungee.api.chat.TextComponent;

public class Util {

	private static final String RESET = "\033[0m";
	
	public static String color(String msg) {
		return ChatColor.translateAlternateColorCodes('&', msg);
	}

	public static List<String> colorList(List<String> l) {
		return l.stream().map(Util::color).collect(Collectors.toList());
	}

	public static String uncolor(String message) {
		return message.replaceAll("(?i)(&|" + ChatColor.COLOR_CHAR + ")[0-9A-FK-OR]", "");
	}

	public static List<String> uncolorList(List<String> l) {
		return l.stream().map(Util::uncolor).collect(Collectors.toList());
	}

	public static long generateRandomNumber(double n) {
		long min = (long) Math.pow(10, n - 1);
		return ThreadLocalRandom.current().nextLong(min, min * 10);
	}

	public static void info(String info) {
		MisterLib.check();
		String ns = uncolor("\033[36m" + info + RESET);
		MisterLib.getInstance().getLogger().info(ns);
	}

	public static Void error(String error) {
		MisterLib.check();
		String ns = uncolor("\033[31m" + error + RESET);
		MisterLib.getInstance().getLogger().severe(ns);
		return null;
	}

	public static void exception(Throwable ex) {
		MisterLib.check();
		String ns = uncolor("\033[33m" + ex + RESET);
		MisterLib.getInstance().getLogger().warning(ns);
	}

	public static void sendComp(Player sender, TextComponent msg) {
		sender.spigot().sendMessage(msg);
	}

	public static List<String> readUrl(String surl, Proxy proxy) {
		try {
			URL url = new URL(surl);
			URLConnection con = url.openConnection(proxy);
			con.setConnectTimeout(2000);
			con.setReadTimeout(2000);

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			List<String> rs = in.lines().collect(Collectors.toList());
			in.close();
			return rs;
		} catch (Exception e) {
			exception(e);
			return Lists.newArrayList();
		}
	}

	public static List<String> readUrl(String surl) {
		return readUrl(surl, Proxy.NO_PROXY);
	}

	public static String readJsonUrl(String surl) {
		return readUrl(surl).stream().collect(Collectors.joining());
	}

	public static <T> List<T> add(List<T> list, T object) {
		if (list == null)
			list = Lists.newArrayList();
		list.add(object);
		return list;
	}

}
