package me.mrmaurice.lib.reflections;

import java.util.Arrays;

public enum Version {

	UNSUPPORTED("", 0),
	v8("v1_8_R3", 8),
	v9("v1_9_R2", 9),
	v10("v1_10_R1", 10),
	v11("v1_11_R1", 11),
	v12("v1_12_R1", 12),
	v13("v1_13_R2", 13);

	private String realName;
	private int numVersion;

	Version(String value, int ver) {
		realName = value;
		numVersion = ver;
	}

	public boolean isAbove(Version ver) {
		return ver.numVersion > numVersion;
	}

	public boolean isBelow(Version ver) {
		return ver.numVersion < numVersion;
	}

	public static Version get(String name) {
		return Arrays.stream(values()).filter(v -> v.realName.equals(name)).findFirst().orElse(UNSUPPORTED);
	}

	public ReClass getNetClass(String clazz) {
		return Reflections.getClass("net.minecraft.server." + this + clazz);
	}

	public ReClass getCraftClass(String clazz) {
		return Reflections.getClass("org.bukkit.craftbukkit." + this + clazz);
	}

	public ReClass getVersionClass(String clazz) {
		return Reflections.getClass(Reflections.getVersioningPackage() + "." + this + clazz);
	}

	@Override
	public String toString() {
		return realName + ".";
	}

}