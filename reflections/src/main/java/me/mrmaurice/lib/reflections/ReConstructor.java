package me.mrmaurice.lib.reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.util.function.Predicate;

public class ReConstructor implements CachedObject<Constructor<?>> {

	private Constructor<?> cons;

	public ReConstructor(Constructor<?> constructor) {
		cons = constructor;
		if (!cons.isAccessible())
			cons.setAccessible(true);
	}

	@SuppressWarnings("unchecked")
	public <T> T newInstance(Object... args) {
		try {
			return (T) cons.newInstance(args);
		} catch (Exception e) {
			throw new ReException("Cannot start instance of " + cons.getName());
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
	public Constructor<?> getObject() {
		return cons;
	}

}