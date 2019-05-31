package me.mrmaurice.lib.gui.elements;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Maps;

import lombok.Builder;
import lombok.Getter;
import me.mrmaurice.lib.gui.enums.Columns;
import me.mrmaurice.lib.gui.enums.Rows;

@Builder
public class GUIPane {

	@Getter
	@Builder.Default
	private GUICoord coords = new GUICoord(0, 0);
	@Builder.Default
	private Rows height = Rows.ONE;
	@Builder.Default
	private Columns lenght = Columns.NINE;
	@Builder.Default
	private Map<GUICoord, GUIButton> content = Maps.newHashMap();
	@Getter
	@Builder.Default
	private int priority = 0;
	@Getter
	@Builder.Default
	private boolean disabled = true;

	public void fill(GUIButton butt) {
		for (int x = 0; x < lenght.getValue(); x++)
			for (int y = 0; y < height.getValue(); y++)
				if (get(x, y) == null)
					set(y, x, butt);
	}

	public void add(GUIButton butt) {
		for (int y = 0; y < height.getValue(); y++)
			for (int x = 0; x < lenght.getValue(); x++)
				if (get(x, y) == null) {
					set(y, x, butt);
					return;
				}
	}

	public void add(List<GUIButton> buttons) {
		buttons.forEach(this::add);
	}

	public void set(int y, int x, GUIButton butt) {
		if (y < 0 || y >= height.getValue())
			throw new IllegalArgumentException("The row isnt inside of the pane.");
		if (x < 0 || x >= lenght.getValue())
			throw new IllegalArgumentException("The column isnt inside of the pane.");
		content.put(new GUICoord(x, y), butt);
	}

	public GUIButton get(int x, int y) {
		return content.get(new GUICoord(x, y));
	}

	public GUIButton getButton(int slot) {
		int x = (slot % 9) - coords.getX();
		int y = (slot / 9) - coords.getY();

		if (x < 0 || x > lenght.getValue() || y < 0 || y > height.getValue())
			return null;

		return get(x, y);
	}

	public List<ItemStack> getContent() {
		return content.values().stream().map(GUIButton::toItemStack).collect(Collectors.toList());
	}

	public void setRow(int y, GUIButton button) {
		if (y < 0 || y >= height.getValue())
			throw new IllegalArgumentException("The row isnt in the pane.");
		for (int x = 0; x < lenght.getValue(); x++)
			content.put(new GUICoord(x, y), button);
	}

	public boolean click(InventoryClickEvent event) {
		int slot = event.getSlot();

		GUIButton button = getButton(slot);

		if (button == null)
			return false;

		button.run(event);
		event.setCancelled(disabled);
		return true;
	}

	public void display(Inventory inv) {
		content.forEach((c, b) -> {
			int x = coords.getX() + c.getX();
			int y = coords.getY() + c.getY();
			inv.setItem((y * 9) + x, b.toItemStack());
		});
	}

}
