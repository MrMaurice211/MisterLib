package me.mrmaurice.lib.gui;

import java.util.Map;

import org.bukkit.entity.Player;

import com.google.common.collect.Maps;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class GUI<T> {

	@Setter(AccessLevel.NONE)
	private Map<String, GUI<?>> childs = Maps.newHashMap();
	@Setter(AccessLevel.NONE)
	private Map<String, Object> data = Maps.newHashMap();

	private boolean closeable = true;
	private GUI<?> parent;

	@SuppressWarnings("unchecked")
	public GUI<T> getMainGUI() {
		if (parent == null)
			return this;
		else
			return (GUI<T>) parent.getMainGUI();
	}

	public void addChild(String name, GUI<?> child) {
		child.setParent(this);
		childs.put(name, child);
	}

	@SuppressWarnings("unchecked")
	public GUI<T> getChild(String name) {
		return (GUI<T>) childs.get(name);
	}

	public void setData(String key, Object value) {
		data.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <R> R getData(String key) {
		return (R) data.get(key);
	}
	
	@SuppressWarnings("unchecked")
	public GUI<T> getParent(){
		return (GUI<T>) parent;
	}

	public abstract T build();
	
	public abstract void close(Player player);

}
