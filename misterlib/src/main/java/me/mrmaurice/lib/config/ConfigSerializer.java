package me.mrmaurice.lib.config;

import me.mrmaurice.lib.reflections.Reflections;

public interface ConfigSerializer<T> {

	public static final ConfigSerializer<Object> DEFAULT = Reflections.getClass(DefaultSerializer.class).newInstance();

	public String serialize(T obj);

	public T deserialize(String s);

}
