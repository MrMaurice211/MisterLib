package me.mrmaurice.lib.gui.enums;

import lombok.Getter;

public enum Columns {

	ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7), EIGHT(8), NINE(9);

	@Getter
	final int value;
	
	Columns(int value) {
		this.value = value;
	}
	
	public static Columns valueOf(int i) {
		for (Columns r : values())
			if (r.value == i)
				return r;
		throw new IllegalArgumentException("Columns of size " + i + " is not present.");
	}

}
