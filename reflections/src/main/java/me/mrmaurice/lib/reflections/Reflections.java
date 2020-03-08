package me.mrmaurice.lib.reflections;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import me.mrmaurice.lib.utils.Util;

@UtilityClass
public class Reflections {

	@Setter
	@Getter
	private static String versioningPackage;

	private static Version version;
	private static Set<ReClass> clazzez = Sets.newCopyOnWriteArraySet();

	private static ReClass getCache(Class<?> clazz) {
		return clazzez.stream().filter(c -> c.getObject() == clazz).findFirst().orElse(null);
	}

	private static ReClass getCache(String clazzName) {
		return clazzez.stream().filter(c -> c.getName().equals(clazzName)).findFirst().orElse(null);
	}

	public static Version getVersion() {
		if (version == null) {
			String name = Bukkit.getServer().getClass().getPackage().getName();
			version = Version.get(name.substring(name.lastIndexOf('.') + 1));
		}
		return version;
	}

	public static ReClass getCraftClass(String clazz) {
		return getVersion().getCraftClass(clazz);
	}

	public static ReClass getNetClass(String clazz) {
		return getVersion().getNetClass(clazz);
	}

	public static ReClass getVerClass(String clazz) {
		return getVersion().getVersionClass(clazz);
	}

	public static List<ReClass> getAllClasses(String packageName, File file) {

		List<ReClass> clazzez = Lists.newArrayList();

		try (JarFile jf = new JarFile(file)) {
			Enumeration<JarEntry> entries = jf.entries();

			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement();
				String name = entry.getName().replace('/', '.');
				if (name.startsWith(packageName) && name.endsWith(".class")) {
					String className = name.substring(0, name.length() - 6);
					ReClass clazz = getClass(className);
					clazzez.add(clazz);
				}
			}
		} catch (IOException e) {
			Util.exception(e);
		}
		return clazzez;
	}

	public static List<ReClass> findClasses(File directory, String packageName) {
		List<ReClass> classes = Lists.newArrayList();
		if (!directory.exists())
			return classes;

		File[] files = directory.listFiles();
		for (File file : files) {
			String fileName = Files.getNameWithoutExtension(file.getName());
			if (file.isDirectory())
				classes.addAll(findClasses(file, packageName + '.' + file.getName()));
			else
				classes.add(getClass(packageName.replace('/', '.') + '.' + fileName));
		}

		return classes;
	}

	public static ReClass getClass(Object obj) {
		return getClass(obj.getClass());
	}

	public static ReClass getClass(Class<?> clazz) {
		ReClass rc = getCache(clazz);

		if (rc == null) {
			rc = new ReClass(clazz);
			clazzez.add(rc);
		}

		return rc;
	}

	public static ReClass getClass(String clazzName) {
		ReClass cc = getCache(clazzName);

		if (cc != null)
			return cc;

		try {
			Class<?> clazz = Class.forName(clazzName);
			return getClass(clazz);
		} catch (Exception e) {
			throw new ReException("Cannot find the class " + clazzName);
		}
	}

	public static Object getConnection(Player player) {
		ReMethod getHandle = getMethod(player.getClass(), "getHandle");
		if (getHandle == null)
			return null;
		Object entityPlayer = getHandle.invoke(player);
		ReField playerConn = getField(entityPlayer.getClass(), "playerConnection");
		return playerConn == null ? null : playerConn.get(entityPlayer);
	}

	public static void broadcastPacket(Object packet) {
		Bukkit.getOnlinePlayers().forEach(p -> sendPacket(packet, p));
	}

	public static void sendPacket(Object packet, Player player) {

		if (packet == null)
			return;

		ReClass packetClazz = getNetClass("Packet");
		if (packetClazz == null)
			return;
		Object conn = getConnection(player);
		if (conn == null)
			return;
		// ReMethod send = getMethod(conn.getClass(), "sendPacket",
		// packetClazz.getObject());
		// if (send != null)
		// send.invoke(conn, packet);
		getMethod(conn.getClass(), "sendPacket", packetClazz.getObject()).invoke(conn, packet);
	}

	public static ReConstructor getConstructor(Class<?> clazz, Class<?>... params) {

		Predicate<Constructor<?>> filter = c -> Arrays.equals(params, c.getParameterTypes());

		ReClass cache = getCache(clazz);

		if (cache != null) {

			Optional<ReConstructor> ccs = cache.getCacheOf(ReConstructor.class).filter(c -> c.isApplicable(filter))
					.findFirst();

			if (ccs.isPresent())
				return ccs.get();
		}

		Optional<Constructor<?>> mt = Arrays.stream(clazz.getDeclaredConstructors()).filter(filter).findFirst();

		if (mt.isPresent())
			return new ReConstructor(mt.get());

		if (clazz.getSuperclass() != null)
			return getConstructor(clazz.getSuperclass(), params);

		throw new ReException("Cannot find Constructor with parameters " + params);
	}

	public static ReMethod getMethod(String clazz, String methodName, Class<?>... params) {
		if (clazz.startsWith("{obc}"))
			return getCraftClass(clazz.split("\\.")[1]).getMethod(methodName, params);
		if (clazz.startsWith("{nms}"))
			return getNetClass(clazz.split("\\.")[1]).getMethod(methodName, params);
		return getMethod(getClass(clazz).getObject(), methodName, params);
	}

	public static ReMethod getMethod(Class<?> clazz, Predicate<Method> filter, Class<?>... params) {

		if (clazz.getDeclaredMethods().length == 0)
			throw new ReException("There is no methods on the class " + clazz.getSimpleName());

		if (params.length > 0)
			filter = filter.and(m -> Arrays.equals(params, m.getParameterTypes()));

		Predicate<Method> ff = filter;

		ReClass cache = getCache(clazz);

		if (cache != null) {
			Optional<ReMethod> mi = cache.getCacheOf(ReMethod.class).filter(i -> i.isApplicable(ff)).findFirst();

			if (mi.isPresent())
				return mi.get();
		}

		Optional<Method> mt = Arrays.stream(clazz.getDeclaredMethods()).filter(ff).findFirst();

		if (mt.isPresent())
			return new ReMethod(mt.get());

		if (clazz.getSuperclass() != null)
			return getMethod(clazz.getSuperclass(), ff, params);

		throw new ReException("Cannot find Method in Class " + clazz.getSimpleName());

	}

	public static ReMethod getMethod(Class<?> clazz, String methodName, Class<?>... params) {
		return getMethod(clazz, m -> m.getName().equalsIgnoreCase(methodName), params);
	}

	public static ReMethod getMethod(Class<?> clazz, Class<?> returnType, Class<?>... params) {
		return getMethod(clazz, m -> m.getReturnType() == returnType, params);
	}

	public static ReField getField(String clazz, String fieldName) {
		if (clazz.startsWith("{obc}"))
			return getCraftClass(clazz.split("\\.")[1]).getField(fieldName);
		if (clazz.startsWith("{nms}"))
			return getNetClass(clazz.split("\\.")[1]).getField(fieldName);
		return getClass(clazz).getField(fieldName);
	}

	public static ReField getField(String clazz, Class<?> fieldType) {
		if (clazz.startsWith("{obc}"))
			return getCraftClass(clazz.split("\\.")[1]).getField(fieldType);
		if (clazz.startsWith("{nms}"))
			return getNetClass(clazz.split("\\.")[1]).getField(fieldType);
		return getClass(clazz).getField(fieldType);
	}

	public static ReField getField(Class<?> clazz, Predicate<Field> filter) {

		ReClass cache = getCache(clazz);

		if (cache != null) {
			Optional<ReField> fa = cache.getCacheOf(ReField.class).filter(i -> i.isApplicable(filter)).findFirst();

			if (fa.isPresent())
				return fa.get();
		}

		Optional<Field> fd = Arrays.stream(clazz.getDeclaredFields()).filter(filter).findFirst();

		if (fd.isPresent())
			return new ReField(fd.get());

		if (clazz.getSuperclass() != null)
			return getField(clazz.getSuperclass(), filter);

		throw new ReException("Cannot find Field in Class " + clazz.getSimpleName());
	}

	public static ReField getField(Class<?> clazz, String fieldName) {
		return getField(clazz, f -> f.getName().equals(fieldName));
	}

	public static ReField getField(Class<?> clazz, Class<?> fieldType) {
		return getField(clazz, f -> fieldType.isAssignableFrom(f.getType()));
	}

	public static <T extends Annotation> T getAnot(CachedObject<?> obj, Class<T> type) {
		return obj.getAnnot(type);
	}

}
