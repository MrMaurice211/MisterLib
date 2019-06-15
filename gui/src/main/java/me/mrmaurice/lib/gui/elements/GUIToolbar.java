package me.mrmaurice.lib.gui.elements;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GUIToolbar {

	private Map<Integer, GUIButton> toolbar = Maps.newHashMapWithExpectedSize(9);
	@Getter
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

	public GUIToolbar merge(GUIToolbar... others) {
		List<GUIToolbar> list = Lists.newArrayList(others);
		list.add(this);
		list.sort(Comparator.comparingInt(GUIToolbar::getPriority));
		GUIToolbar toolbar = new GUIToolbar();
		list.forEach(other -> toolbar.toolbar.putAll(other.toolbar));
		return toolbar;
	}

}
