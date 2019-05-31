package me.mrmaurice.lib.reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import com.google.common.collect.Lists;

public class ReClass implements CachedObject<Class<?>> {

	private Class<?> object;
	private List<CachedObject<?>> objects;

	public ReClass(Class<?> clazz) {
		object = clazz;
		objects = Lists.newArrayList();
	}

	public <T> T newInstance() {
		return getConstructor().newInstance();
	}

	public ReConstructor getConstructor(Class<?>... params) {
		ReConstructor invoker = Reflections.getConstructor(object, params);
		objects.add(invoker);
		return invoker;
	}

	public ReMethod getMethod(String methodName, Class<?>... params) {
		ReMethod invoker = Reflections.getMethod(object, methodName, params);
		objects.add(invoker);
		return invoker;
	}

	public ReMethod getMethod(Class<?> returnType, Class<?>... params) {
		ReMethod invoker = Reflections.getMethod(object, returnType, params);
		objects.add(invoker);
		return invoker;
	}

	public ReField getField(String fieldName) {
		ReField accessor = Reflections.getField(object, fieldName);
		objects.add(accessor);
		return accessor;
	}

	public ReField getField(Class<?> fieldType) {
		ReField accessor = Reflections.getField(object, fieldType);
		objects.add(accessor);
		return accessor;
	}

	public List<ReField> getFieldsOfType(Class<?> type) {
		List<ReField> fields = Lists.newArrayList();
		for (Field f : object.getDeclaredFields()) {
			if (type == null || f.getType() == type)
				fields.add(new ReField(f));
		}
		Class<?> superC = object.getSuperclass();
		if (superC != null && superC != Object.class)
			fields.addAll(Reflections.getClass(superC).getFieldsOfType(type));
		return fields;
	}

	public List<ReField> getFields() {
		return getFieldsOfType(null);
	}

	public ReField getListOfType(Class<?> type) {
		List<ReField> fields = getFieldsOfType(List.class);
		for (ReField f : fields) {
			ParameterizedType integerListType = (ParameterizedType) f.getObject().getGenericType();
			if (type == integerListType.getActualTypeArguments()[0])
				return f;
		}
		return null;
	}

	public <T extends CachedObject<?>> Stream<T> getCacheOf(Class<T> type) {
		return objects.stream().filter(o -> o.getClass().isAssignableFrom(type)).map(type::cast);
	}

	@Override
	public Class<?> getObject() {
		return object;
	}

	@Override
	public String getName() {
		return object.getName();
	}

	@Override
	public <A extends Annotation> A getAnnot(Class<A> type) {
		return object.getDeclaredAnnotation(type);
	}

	@Override
	public boolean isApplicable(Predicate<Class<?>> filter) {
		return filter.test(object);
	}

}
