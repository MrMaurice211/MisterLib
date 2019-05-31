package me.mrmaurice.lib.utils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.io.Files;

public class StringUtil {

	public static String cutName(String name) {
		name = name.length() > 16 ? name.substring(0, 16) : name;
		return name;
	}

	public static String joinString(String[] text, String sep) {
		return Arrays.stream(text).collect(Collectors.joining(sep));
	}

	public static String joinString(String[] text) {
		return joinString(text, " ");
	}

	public static int getIndex(String line, List<String> list) {
		for (String s : list)
			if (s.startsWith(line) || s.equalsIgnoreCase(line))
				return list.indexOf(s);
		return -1;
	}

	public static int isInt(String s) {
		try {
			return Integer.valueOf(s);
		} catch (Exception e) {
			return -1;
		}
	}

	public static String getFileName(File file) {
		return Files.getNameWithoutExtension(file.getName());
	}

	public static String getFileExt(File file) {
		return Files.getFileExtension(file.getName());
	}

	public static String capit(String str) {
		if (str == null || str.isEmpty())
			return str;

		String[] a = str.split(" ");

		if (a.length == 0)
			return Character.toTitleCase(str.charAt(0)) + str.substring(1).toLowerCase();
		str = "";
		for (String s : a)
			str += Character.toTitleCase(s.charAt(0)) + s.substring(1).toLowerCase() + " ";

		return str.trim();
	}

}
