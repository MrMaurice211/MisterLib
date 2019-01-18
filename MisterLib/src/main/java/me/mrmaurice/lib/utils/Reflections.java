package me.mrmaurice.lib.utils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

public class Reflections {

	private Reflections() {}

	private static Version version;
	private static Set<ReClass> clazzez = Sets.newCopyOnWriteArraySet();

	private static ReClass getCache(Class<?> clazz) {
		return clazzez.stream().filter(c -> c.get() == clazz).findFirst().orElse(null);
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
		return getClass("org.bukkit.craftbukkit." + getVersion() + clazz);
	}

	public static ReClass getNetClass(String clazz) {
		return getClass("net.minecraft.server." + getVersion() + clazz);
	}

	public static ReClass getHologramClass() {
		return getClass("me.mrmaurice.bnpc.nms." + getVersion() + "EntityNMSHologram");
	}

	public static ReClass getNPCClass() {
		return getClass("me.mrmaurice.bnpc.nms." + getVersion() + "EntityNMSNPC");
	}

	public static ReClass getBridgeClass() {
		return getClass("me.mrmaurice.bnpc.nms." + getVersion() + "NMSImp");
	}

	public static List<ReClass> getAllClasses(String packageName, File file) throws IOException {

		List<ReClass> clazzez = Lists.newArrayList();

		JarFile jf = new JarFile(file);
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
		jf.close();
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
			throw new ReflectionException("Cannot find the class " + clazzName);
		}
	}

	public static Object getConnection(Player player) {
		Object entityPlayer = getMethod(player.getClass(), "getHandle").invoke(player);
		return getField(entityPlayer.getClass(), "playerConnection").get(entityPlayer);
	}

	public static void broadcastPacket(Object packet) {
		Bukkit.getOnlinePlayers().forEach(p -> sendPacket(packet, p));
	}

	public static void sendPacket(Object packet, Player player) {

		if (packet == null)
			return;

		ReClass packetClazz = getNetClass("Packet");
		Object conn = getConnection(player);
		getMethod(conn.getClass(), "sendPacket", packetClazz.get()).invoke(conn, packet);
	}

	public static ConsInvoker getConstructor(Class<?> clazz, Class<?>... params) {

		Predicate<Constructor<?>> filter = c -> Arrays.equals(params, c.getParameterTypes());

		ReClass cache = getCache(clazz);

		if (cache != null) {

			Optional<ConsInvoker> ccs = cache.getCacheOf(ConsInvoker.class).filter(c -> c.isApplicable(filter))
					.findFirst();

			if (ccs.isPresent())
				return ccs.get();
		}

		Optional<Constructor<?>> mt = Arrays.stream(clazz.getDeclaredConstructors()).filter(filter).findFirst();

		if (mt.isPresent())
			return new Reflections.ConsInvoker(mt.get());

		if (clazz.getSuperclass() != null)
			return getConstructor(clazz.getSuperclass(), params);

		throw new ReflectionException("Cannot find Constructor with parameters " + params);
	}

	public static MethodInvoker getMethod(String clazz, String methodName, Class<?>... params) {
		if (clazz.startsWith("{obc}"))
			return getCraftClass(clazz.split("\\.")[1]).getMethod(methodName, params);
		if (clazz.startsWith("{nms}"))
			return getNetClass(clazz.split("\\.")[1]).getMethod(methodName, params);
		return getMethod(getClass(clazz).get(), methodName, params);
	}

	public static MethodInvoker getMethod(Class<?> clazz, Predicate<Method> filter, Class<?>... params) {

		if (clazz.getDeclaredMethods().length == 0)
			return null;

		if (params.length > 0)
			filter = filter.and(m -> Arrays.equals(params, m.getParameterTypes()));

		Predicate<Method> ff = filter;

		ReClass cache = getCache(clazz);

		if (cache != null) {
			Optional<MethodInvoker> mi = cache.getCacheOf(MethodInvoker.class).filter(i -> i.isApplicable(ff))
					.findFirst();

			if (mi.isPresent())
				return mi.get();
		}

		Optional<Method> mt = Arrays.stream(clazz.getDeclaredMethods()).filter(ff).findFirst();

		if (mt.isPresent())
			return new Reflections.MethodInvoker(mt.get());

		if (clazz.getSuperclass() != null)
			return getMethod(clazz.getSuperclass(), ff, params);

		throw new ReflectionException("Cannot find Method in Class " + clazz.getSimpleName());

	}

	public static MethodInvoker getMethod(Class<?> clazz, String methodName, Class<?>... params) {
		return getMethod(clazz, m -> m.getName().equalsIgnoreCase(methodName), params);
	}

	public static MethodInvoker getMethod(Class<?> clazz, Class<?> returnType, Class<?>... params) {
		return getMethod(clazz, m -> m.getReturnType() == returnType, params);
	}

	public static FieldAccessor getField(String clazz, String fieldName) {
		if (clazz.startsWith("{obc}"))
			return getCraftClass(clazz.split("\\.")[1]).getField(fieldName);
		if (clazz.startsWith("{nms}"))
			return getNetClass(clazz.split("\\.")[1]).getField(fieldName);
		return getClass(clazz).getField(fieldName);
	}

	public static FieldAccessor getField(String clazz, Class<?> fieldType) {
		if (clazz.startsWith("{obc}"))
			return getCraftClass(clazz.split("\\.")[1]).getField(fieldType);
		if (clazz.startsWith("{nms}"))
			return getNetClass(clazz.split("\\.")[1]).getField(fieldType);
		return getClass(clazz).getField(fieldType);
	}

	public static FieldAccessor getField(Class<?> clazz, Predicate<Field> filter) {

		ReClass cache = getCache(clazz);

		if (cache != null) {
			Optional<FieldAccessor> fa = cache.getCacheOf(FieldAccessor.class).filter(i -> i.isApplicable(filter))
					.findFirst();

			if (fa.isPresent())
				return fa.get();
		}

		Optional<Field> fd = Arrays.stream(clazz.getDeclaredFields()).filter(filter).findFirst();

		if (fd.isPresent())
			return new Reflections.FieldAccessor(fd.get());

		if (clazz.getSuperclass() != null)
			return getField(clazz.getSuperclass(), filter);

		throw new ReflectionException("Cannot find Field in Class " + clazz.getSimpleName());
	}

	public static FieldAccessor getField(Class<?> clazz, String fieldName) {
		return getField(clazz, f -> f.getName().equals(fieldName));
	}

	public static FieldAccessor getField(Class<?> clazz, Class<?> fieldType) {
		return getField(clazz, f -> fieldType.isAssignableFrom(f.getType()));
	}

	public static <T extends Annotation> T getAnot(CachedObject<?> obj, Class<T> type) {
		return obj.getAnnot(type);
	}

	public static interface CachedObject<T> {

		public T get();

		public String getName();

		public <A extends Annotation> A getAnnot(Class<A> type);

		public boolean isApplicable(Predicate<T> filter);
	}

	public static class FieldAccessor implements CachedObject<Field> {

		private Field f;

		public FieldAccessor(Field field) {
			f = field;
			if (!f.isAccessible())
				f.setAccessible(true);
		}

		public FieldAccessor removeFinal() {
			if (Modifier.isFinal(f.getModifiers())) {
				try {
					Field modifiersField = Field.class.getDeclaredField("modifiers");
					modifiersField.setAccessible(true);
					modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
				} catch (Exception e) {
					throw new Reflections.ReflectionException("Cannot remove the final value to " + f.getName());
				}
			}
			return this;
		}

		@SuppressWarnings("unchecked")
		public <T> T get(Object obj) {
			try {
				return (T) f.get(obj);
			} catch (Exception e) {
				throw new Reflections.ReflectionException("Cannot get the value of field " + f.getName());
			}
		}

		public void set(Object obj, Object value) {
			try {
				f.set(obj, value);
			} catch (Exception e) {
				e.printStackTrace();
				throw new Reflections.ReflectionException("Cannot set the value of field " + f.getName());
			}
		}

		@Override
		public boolean isApplicable(Predicate<Field> filter) {
			return filter.test(f);
		}

		@Override
		public String getName() {
			return f.getName();
		}

		@Override
		public <T extends Annotation> T getAnnot(Class<T> type) {
			return f.getDeclaredAnnotation(type);
		}

		@Override
		public Field get() {
			return f;
		}

	}

	public static class MethodInvoker implements CachedObject<Method> {

		private Method m;

		public MethodInvoker(Method method) {
			m = method;
			if (!m.isAccessible())
				m.setAccessible(true);
		}

		@SuppressWarnings("unchecked")
		public <T> T invoke(Object obj, Object... args) {
			try {
				return (T) m.invoke(obj, args);
			} catch (Exception e) {
				throw new Reflections.ReflectionException("Cannot invoke the method " + m.getName());
			}
		}

		public <A extends Annotation> A getAnnot(Class<A> anot) {
			return m.getDeclaredAnnotation(anot);
		}

		@Override
		public boolean isApplicable(Predicate<Method> filter) {
			return filter.test(m);
		}

		@Override
		public String getName() {
			return m.getName();
		}

		@Override
		public Method get() {
			return m;
		}

	}

	public static class ConsInvoker implements CachedObject<Constructor<?>> {

		private Constructor<?> cons;

		public ConsInvoker(Constructor<?> constructor) {
			cons = constructor;
			if (!cons.isAccessible())
				cons.setAccessible(true);
		}

		@SuppressWarnings("unchecked")
		public <T> T newInstance(Object... args) {
			try {
				return (T) cons.newInstance(args);
			} catch (Exception e) {
				throw new Reflections.ReflectionException("Cannot start instance of " + cons.getName());
			}
		}

		@Override
		public boolean isApplicable(Predicate<Constructor<?>> filter) {
			return filter.test(cons);
		}

		@Override
		public String getName() {
			return cons.getName();
		}

		@Override
		public <A extends Annotation> A getAnnot(Class<A> type) {
			return cons.getDeclaredAnnotation(type);
		}

		@Override
		public Constructor<?> get() {
			return cons;
		}

	}

	public static class ReClass implements CachedObject<Class<?>> {

		private Class<?> parent;
		private List<CachedObject<?>> objects;

		public ReClass(Class<?> clazz) {
			parent = clazz;
			objects = Lists.newArrayList();
		}

		public <T> T newInstance() {
			return getConstructor().newInstance();
		}

		public ConsInvoker getConstructor(Class<?>... params) {
			ConsInvoker invoker = Reflections.getConstructor(parent, params);
			objects.add(invoker);
			return invoker;
		}

		public MethodInvoker getMethod(String methodName, Class<?>... params) {
			MethodInvoker invoker = Reflections.getMethod(parent, methodName, params);
			objects.add(invoker);
			return invoker;
		}

		public MethodInvoker getMethod(Class<?> returnType, Class<?>... params) {
			MethodInvoker invoker = Reflections.getMethod(parent, returnType, params);
			objects.add(invoker);
			return invoker;
		}

		public FieldAccessor getField(String fieldName) {
			FieldAccessor accessor = Reflections.getField(parent, fieldName);
			objects.add(accessor);
			return accessor;
		}

		public FieldAccessor getField(Class<?> fieldType) {
			FieldAccessor accessor = Reflections.getField(parent, fieldType);
			objects.add(accessor);
			return accessor;
		}

		public List<FieldAccessor> getFields() {
			List<FieldAccessor> fields = Lists.newArrayList();
			Arrays.stream(parent.getDeclaredFields()).map(FieldAccessor::new).forEach(fields::add);
			Class<?> superC = parent.getSuperclass();
			if (superC != null && superC != Object.class)
				fields.addAll(Reflections.getClass(superC).getFields());
			return fields;
		}

		public <T extends CachedObject<?>> Stream<T> getCacheOf(Class<T> type) {
			return objects.stream().filter(o -> o.getClass().isAssignableFrom(type)).map(type::cast);
		}

		@Override
		public Class<?> get() {
			return parent;
		}

		@Override
		public String getName() {
			return parent.getName();
		}

		@Override
		public <A extends Annotation> A getAnnot(Class<A> type) {
			return parent.getDeclaredAnnotation(type);
		}

		@Override
		public boolean isApplicable(Predicate<Class<?>> filter) {
			return filter.test(parent);
		}

	}

	public static class ReflectionException extends RuntimeException {

		private static final long serialVersionUID = 1L;

		public ReflectionException(String s) {
			super(s);
		}

	}

	public enum Version {

		UNSUPPORTED(
				""),
		v8("v1_8_R3"),
		v9("v1_9_R2"),
		v10("v1_10_R1"),
		v11("v1_11_R1"),
		v12("v1_12_R1"),
		v13("v1_13_R2");

		private String realName;

		Version(String value) {
			realName = value;
		}

		public static Version get(String name) {
			return Arrays.stream(values()).filter(v -> v.realName.equals(name)).findFirst().orElse(UNSUPPORTED);
		}

		@Override
		public String toString() {
			return realName + ".";
		}

	}

}
