package me.mrmaurice.lib.gui.elements;

import java.util.Map;

import com.google.common.collect.Maps;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GUIToolbar {

	private Map<Integer, GUIButton> toolbar = Maps.newHashMapWithExpectedSize(8);
	private final int priority;

	public GUIToolbar() {
		this(1);
	}

	public void set(Integer slot, GUIButton butt) {
		if (slot < 0 || slot > 8)
			return;
		toolbar.put(slot, butt);
	}

	public void add(GUIButton butt) {
		for (int i = 0; i < 9; i++)
			if (toolbar.get(i) == null) {
				toolbar.put(i, butt);
				break;
			}
	}

	public void fill(GUIButton butt) {
		for (int i = 0; i < 9; i++)
			if (toolbar.get(i) == null)
				toolbar.put(i, butt);
	}

	public void merge(GUIToolbar other) {
		if (other.priority >= priority)
			toolbar.putAll(other.toolbar);
		else
			other.toolbar.putAll(toolbar);
	}

}
