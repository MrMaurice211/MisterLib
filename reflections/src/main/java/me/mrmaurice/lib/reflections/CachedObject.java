package me.mrmaurice.lib.reflections;

import java.lang.annotation.Annotation;
import java.util.function.Predicate;

public interface CachedObject<T> {

	public T getObject();

	public String getName();

	public <A extends Annotation> A getAnnot(Class<A> type);

	public boolean isApplicable(Predicate<T> filter);
}
