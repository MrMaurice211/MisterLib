package me.mrmaurice.lib.config;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigSetting {

	public static final ConfigSetting DEFAULT = new ConfigSetting() {

		@Override
		public Class<? extends Annotation> annotationType() {
			return ConfigSetting.class;
		}

		@Override
		public ConfigLang type() {
			return ConfigLang.JSON;
		}

		@Override
		public String objectName() {
			return "";
		}

		@Override
		public String fieldName() {
			return "";
		}

		@Override
		public boolean exclude() {
			return false;
		}

		@Override
		public Class<? extends ConfigSerializer<?>> serializer() {
			return DefaultSerializer.class;
		}

	};

	public ConfigLang type() default ConfigLang.JSON;

	public String objectName() default "";

	public String fieldName() default "";

	public boolean exclude() default false;

	public Class<? extends ConfigSerializer<?>> serializer() default DefaultSerializer.class;

}
