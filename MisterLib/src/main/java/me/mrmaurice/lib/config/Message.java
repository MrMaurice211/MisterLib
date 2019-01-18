package me.mrmaurice.lib.config;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.bukkit.command.CommandSender;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import me.mrmaurice.lib.MisterLib;
import me.mrmaurice.lib.utils.Util;

public class Message {

	private boolean color = true;
	private String msg;
	private Pattern patt = Pattern.compile("[%]([^%_]+)[%]");

	private List<Message> toAppend = Lists.newArrayList();
	private Map<String, String> vars = Maps.newHashMap();

	public static <E extends Enum<E>> Message of(Enum<E> value) {
		String path = MisterLib.getConverters().get(value.getClass()).apply(value);
		return of(path);
	}

	public static Message of(String string) {
		Message msg = new Message();
		msg.msg = ConfigManager.of("config").get(string, string);
		return msg;
	}

	public Message var(String place, Object replace) {
		if (replace == null) {
			vars.put(place, null);
			return this;
		}
		vars.put(place, replace.toString());
		return this;
	}

	public Message pref() {
		msg = prefix + msg;
		return this;
	}

	public Message color() {
		color = true;
		return this;
	}

	public Message append(Message msg) {
		toAppend.add(msg);
		return this;
	}

	public Void send(CommandSender sender) {
		sender.sendMessage(toString());
		return null;
	}

	@Override
	public String toString() {
		msg = msg + toAppend.stream().map(m -> m.toString()).collect(Collectors.joining());

		Matcher matcher = patt.matcher(msg);
		while (matcher.find()) {
			String var = matcher.group();
			String value = vars.get(var.replace("%", ""));
			if (value == null)
				continue;
			msg = msg.replace(var, value);
		}
		if (color)
			return Util.color(msg);
		return msg;
	}

	private static String prefix = ConfigManager.of("config").get("prefix", "&9&lServer &7» ");

}
