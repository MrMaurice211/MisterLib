package me.mrmaurice.lib.config;

import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonConfig {

	// private static JsonParser parser = new JsonParser();
	private static Gson pretty = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
	private Map<String, Object> values = Maps.newHashMap();
	// private static Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	//
	// private Map<String, JsonElement> values = Maps.newHashMap();
	// private ReConstructor cons;
	// private JsonObject jsonObject;
	// private File file;
	//
	// public JsonConfig(File file) {
	// if (!file.exists())
	// try {
	// file.getParentFile().mkdirs();
	// file.createNewFile();
	// } catch (Exception ignored) {}
	//
	// this.file = file;
	// jsonObject = new JsonObject();
	// cons = Reflections.getConstructor(JsonPrimitive.class, Object.class);
	// }
	//
	// public <T> T toObject(Class<T> type) {
	// return gson.fromJson(jsonObject, type);
	// }
	//
	// public JsonValue get(String key) {
	// return new JsonValue(jsonObject.get(key));
	// }
	//
	// public SerializableJsonValue getSerializable(String key) {
	// return new SerializableJsonValue(jsonObject.get(key));
	// }
	//
	// public TypeJsonValue getJson(String key) {
	// return new TypeJsonValue(jsonObject.get(key));
	// }
	//
	// public JsonConfig set(String key, Object value) {
	// values.put(key, cons.newInstance(value));
	// return this;
	// }
	//
	// public JsonConfig set(String key, ConfigurationSerializable value) {
	// return set(key, gson.toJson(value.serialize()));
	// }
	//
	// public JsonConfig setJson(String key, Object obj) {
	// return set(key, gson.toJson(obj));
	// }
	//
	// public <T> JsonConfig setIf(String key, T value, Predicate<T> cond) {
	// if (cond.test(value))
	// set(key, value);
	// return this;
	// }
	//
	// public JsonConfig load() {
	// String json = FileUtil.readJsonFromFile(file);
	//
	// if (json.isEmpty())
	// return null;
	//
	// try {
	// jsonObject = parser.parse(json).getAsJsonObject();
	// return this;
	// } catch (Exception e) {
	// return null;
	// }
	// }
	//
	// public void save() {
	// values.forEach((v, j) -> jsonObject.add(v, j));
	// String json = pretty.toJson(jsonObject);
	// FileUtil.writeFile(file, json);
	// }
	//
	// public void delete() {
	// file.deleteOnExit();
	// }
	//
	// public class JsonValue {
	//
	// Object obj;
	//
	// public JsonValue(JsonElement element) {
	// if (element == null) {
	// obj = null;
	// return;
	// }
	// JsonPrimitive primitive = element.getAsJsonPrimitive();
	// obj = Reflections.getField(primitive.getClass(), "value").get(primitive);
	// }
	//
	// @SuppressWarnings("unchecked")
	// public <T> T get(T def) {
	// if (obj == null)
	// return def;
	// return (T) obj;
	// }
	// }
	//
	// public class SerializableJsonValue extends JsonValue {
	//
	// public SerializableJsonValue(JsonElement primitive) {
	// super(primitive);
	// }
	//
	// @SuppressWarnings("unchecked")
	// public <T extends ConfigurationSerializable> T getSer(T def) {
	// if (obj == null)
	// return def;
	// ReMethod deserialize = Reflections.getMethod(def.getClass(), "deserialize",
	// Map.class);
	// Map<String, Object> map = gson.fromJson((String) obj, Map.class);
	// Object obj = deserialize.invoke(null, map);
	// if (obj == null)
	// return def;
	// return (T) obj;
	// }
	//
	// }
	//
	// public class TypeJsonValue extends JsonValue {
	//
	// public TypeJsonValue(JsonElement primitive) {
	// super(primitive);
	// }
	//
	// @Override
	// @SuppressWarnings("unchecked")
	// public <T> T get(T def) {
	// if (obj == null)
	// return def;
	// Object result = gson.fromJson((String) obj, def.getClass());
	// if (result == null)
	// return def;
	// return (T) result;
	// }
	//
	// }

}
