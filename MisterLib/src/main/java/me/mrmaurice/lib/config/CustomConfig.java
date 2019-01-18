package me.mrmaurice.lib.config;

import java.io.File;
import java.io.FileReader;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.io.Files;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

import me.mrmaurice.lib.utils.Reflections;
import me.mrmaurice.lib.utils.Reflections.*;

public class CustomConfig {

	private static final ConsInvoker cons = Reflections.getConstructor(JsonPrimitive.class, Object.class);

	public static ConfigSaver saveTo(File file) {
		return new ConfigSaver(file);
	}

	public static ConfigLoader loadFrom(File file) {
		return new ConfigLoader(file);
	}

	private CustomConfig() {}

	public static class ConfigSaver {

		private File target;

		private ConfigSaver(File file) {
			target = file;
		}

		public void save(Object obj) {

			ReClass clazz = Reflections.getClass(obj.getClass());
			ConfigSetting cs = clazz.getAnnot(ConfigSetting.class);

			if (cs == null)
				cs = ConfigSetting.DEFAULT;

			ConfigLang cl = cs.type();
			try {
				if (cl == ConfigLang.JSON)
					saveAsJson(obj);
				if (cl == ConfigLang.YML)
					saveAsYml(clazz);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		private void saveAsJson(Object obj) throws Exception {
			JsonObject json = new JsonObject();

			ReClass clazz = Reflections.getClass(obj.getClass());
			List<FieldAccessor> fields = clazz.getFields();
			for (FieldAccessor f : fields) {

				ConfigSetting cs = f.getAnnot(ConfigSetting.class);

				if (cs == null)
					cs = ConfigSetting.DEFAULT;

				if (cs.exclude())
					continue;

				String fieldName = cs.fieldName();
				fieldName = fieldName.isEmpty() ? f.getName() : fieldName;
				Object value = f.get(obj);
				ConfigSerializer<Object> ser = Reflections.getClass(cs.serializer()).getConstructor().newInstance();
				json.add(fieldName, cons.newInstance(ser.serialize(value)));

			}
			ConfigSerializer<Object> ser = ConfigSerializer.DEFAULT;
			Files.write(ser.serialize(json), target, Charset.defaultCharset());
		}

		private void saveAsYml(ReClass clazz) {

		}

	}

	public static class ConfigLoader {

		private File source;

		private ConfigLoader(File file) {
			source = file;
		}

		public void load(Object obj) {

			ReClass clazz = Reflections.getClass(obj.getClass());
			ConfigSetting cs = clazz.getAnnot(ConfigSetting.class);

			if (cs == null)
				cs = ConfigSetting.DEFAULT;

			ConfigLang cl = cs.type();

			try {
				if (cl == ConfigLang.JSON)
					loadFromJson(obj);
				if (cl == ConfigLang.YML)
					loadFromYML(obj);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		private void loadFromJson(Object instance) throws Exception {
			JsonObject obj = new JsonParser().parse(new FileReader(source)).getAsJsonObject();

			List<FieldAccessor> fields = Reflections.getClass(instance.getClass()).getFields();
			for (FieldAccessor f : fields) {

				ConfigSetting cs = f.getAnnot(ConfigSetting.class);

				if (cs == null)
					cs = ConfigSetting.DEFAULT;

				if (cs.exclude())
					continue;

				String fieldName = cs.fieldName();
				fieldName = fieldName.isEmpty() ? f.getName() : fieldName;
				ConfigSerializer<?> ser = Reflections.getClass(cs.serializer()).getConstructor().newInstance();
				System.out.println(fieldName);
				System.out.println(obj);
				JsonElement value = obj.get(fieldName);
				System.out.println(value);
				f.set(instance, ser.deserialize(value.getAsString()));
			}
		}

		private void loadFromYML(Object obj) {}

	}

}
