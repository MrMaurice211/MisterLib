package me.mrmaurice.lib.reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Predicate;

public class ReMethod  implements CachedObject<Method> {

	private Method m;

	public ReMethod(Method method) {
		m = method;
		if (!m.isAccessible())
			m.setAccessible(true);
	}

	@SuppressWarnings("unchecked")
	public <T> T invoke(Object instance, Object... args) {
		try {
			return (T) m.invoke(instance, args);
		} catch (Exception e) {
			throw new ReException("Cannot invoke the method " + m.getName());
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
	public Method getObject() {
		return m;
	}

}
