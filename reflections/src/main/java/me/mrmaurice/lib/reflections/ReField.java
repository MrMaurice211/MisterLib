package me.mrmaurice.lib.reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.Predicate;

public class ReField implements CachedObject<Field> {

	private Field f;

	public ReField(Field field) {
		f = field;
		if (!f.isAccessible())
			f.setAccessible(true);
	}

	public ReField removeFinal() {
		if (Modifier.isFinal(f.getModifiers())) {
			try {
				Field modifiersField = Field.class.getDeclaredField("modifiers");
				modifiersField.setAccessible(true);
				modifiersField.setInt(f, f.getModifiers() & ~Modifier.FINAL);
			} catch (Exception e) {
				throw new ReException("Cannot remove the final value to " + f.getName());
			}
		}
		return this;
	}

	@SuppressWarnings("unchecked")
	public <T> T get(Object instance) {
		try {
			return (T) f.get(instance);
		} catch (Exception e) {
			throw new ReException("Cannot get the value of field " + f.getName());
		}
	}

	public void set(Object instance, Object newValue) {
		try {
			f.set(instance, newValue);
		} catch (Exception e) {
			throw new ReException("Cannot set the value of field " + f.getName());
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
	public Field getObject() {
		return f;
	}

}