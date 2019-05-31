package me.mrmaurice.lib.gui.elements;

import lombok.Data;

@Data
public class GUICoord {

	private final int x, y;

	public GUICoord(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
