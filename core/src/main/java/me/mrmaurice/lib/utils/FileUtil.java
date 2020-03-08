package me.mrmaurice.lib.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FileUtil {

	public static List<String> readFile(File file) {
		try {
			return Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
		} catch (Exception e) {
			return Lists.newArrayList();
		}
	}

	public static List<String> readInsideFile(String path) {
		try (InputStream in = FileUtil.class.getResourceAsStream(path);
				BufferedReader input = new BufferedReader(new InputStreamReader(in));) {
			return input.lines().collect(Collectors.toList());
		} catch (Exception e) {
			return Lists.newArrayList();
		}
	}

	public static String readJsonFromFile(File file) {
		return readFile(file).stream().collect(Collectors.joining());
	}

	public static void writeFile(File file, List<String> toWrite) {
		try {
			Files.write(file.toPath(), toWrite, StandardCharsets.UTF_8);
		} catch (Exception e) {
			Util.exception(e);
		}
	}

	public static void writeFile(File file, String... toWrite) {
		writeFile(file, Arrays.asList(toWrite));
	}

}
