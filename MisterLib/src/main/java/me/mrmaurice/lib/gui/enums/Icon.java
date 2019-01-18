package me.mrmaurice.lib.gui.enums;

import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import me.mrmaurice.lib.gui.elements.GUIButton;

public enum Icon {
	PREV("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZjg0ZjU5NzEzMWJiZTI1ZGMwNThhZjg4OGNiMjk4MzFmNzk1OTliYzY3Yzk1YzgwMjkyNWNlNGFmYmEzMzJmYyJ9fX0="),
	NEXT("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZmNmZTg4NDVhOGQ1ZTYzNWZiODc3MjhjY2M5Mzg5NWQ0MmI0ZmMyZTZhNTNmMWJhNzhjODQ1MjI1ODIyIn19fQ=="),
	SERVER("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMjBjZmZjNDdlNDFjYTA5ZGE3YmJjNjM3OTFjNDc0NDg2MWE3YTMzYzY4NDNlNWNhZDk1ZGIyZDljYzk0N2IifX19"),
	ADD("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjA1NmJjMTI0NGZjZmY5OTM0NGYxMmFiYTQyYWMyM2ZlZTZlZjZlMzM1MWQyN2QyNzNjMTU3MjUzMWYifX19"),
	UPLOAD("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjIyMWRhNDQxOGJkM2JmYjQyZWI2NGQyYWI0MjljNjFkZWNiOGY0YmY3ZDRjZmI3N2ExNjJiZTNkY2IwYjkyNyJ9fX0="),
	DOWNLOAD(
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2I4M2JiY2NmNGYwYzg2YjEyZjZmNzk5ODlkMTU5NDU0YmY5MjgxOTU1ZDdlMjQxMWNlOThjMWI4YWEzOGQ4In19fQ=="),
	CLOSE("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYmViNTg4YjIxYTZmOThhZDFmZjRlMDg1YzU1MmRjYjA1MGVmYzljYWI0MjdmNDYwNDhmMThmYzgwMzQ3NWY3In19fQ=="),
	DONE("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYTkyZTMxZmZiNTljOTBhYjA4ZmM5ZGMxZmUyNjgwMjAzNWEzYTQ3YzQyZmVlNjM0MjNiY2RiNDI2MmVjYjliNiJ9fX0="),
	ON("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMzI5NmQzZTE0OTNmYTMyZDgyN2EzNjM1YTY4M2U1YmRlZDY0OTE0ZDc1ZTczYWFjZGNjYmE0NmQ4ZmQ5MCJ9fX0="),
	OFF("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvM2NjNDcwYWUyNjMxZWZkZmFmOTY3YjM2OTQxM2JjMjQ1MWNkN2EzOTQ2NWRhNzgzNmE2YzdhMTRlODc3In19fQ=="),
	DISABLED(
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvODFmYjhjZTY0MDhhNTg1MTM4NGUxYzJlZjc1Mzg1MWVhYzE4YmE0MDE4MjY2Y2RkNjY5ZGM5NDQ4NzNkNDIifX19"),
	SORT("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZTg2ZDcxOWJlZGZlMzdlM2Q3NWM1NzIzZDc1ZTUwN2Y4ZDI0Nzg2MjViZDc4YjMxZjQyMjM0ZDFkNTE2NzZlYyJ9fX0=");

	private String value;

	Icon(String value) {
		this.value = value;
	}

	public GameProfile getProfile() {
		GameProfile profile = new GameProfile(UUID.randomUUID(), null);
		profile.getProperties().put("textures", new Property("textures", value));
		return profile;
	}

	public GUIButton button() {
		return new GUIButton("SKULL_ITEM").withData(3).withProfile(this);
	}

}
