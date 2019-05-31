package me.mrmaurice.lib.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DefaultSerializer implements ConfigSerializer<Object> {

	// private Gson gson = new GsonBuilder().disableHtmlEscaping().create();
	//
	// @Override
	// public String serialize(Object obj) {
	// return gson.toJson(obj);
	// }
	//
	// @Override
	// public Object deserialize(String s) {
	// return gson.fromJson(s, Object.class);
	// }

}
