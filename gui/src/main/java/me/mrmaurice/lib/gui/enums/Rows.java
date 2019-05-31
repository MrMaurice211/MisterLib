package me.mrmaurice.lib.gui.enums;

import lombok.Getter;

public enum Rows {

	ONE(1), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7);

	@Getter
	final int value;

	Rows(int value) {
		this.value = value;
	}

	public static Rows valueOf(int i) {
		for (Rows r : values())
			if (r.value == i)
				return r;
		throw new IllegalArgumentException("Rows of size " + i + " is not present.");
	}

	public Rows plus(int i) {
		if (i > 6 || i < 0)
			return this;
		int sum = value + i;
		if (sum < 1 || i > 7)
			return this;
		return valueOf(sum);
	}

	public Rows minus(int i) {
		if (i > 6 || i < 0)
			return this;
		int sum = value - i;
		if (sum < 1 || i > 7)
			return this;
		return valueOf(sum);
	}

	public int toSlots() {
		return value * 9;
	}

}
