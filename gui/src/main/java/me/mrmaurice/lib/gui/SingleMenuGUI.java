package me.mrmaurice.lib.gui;

import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;
import me.mrmaurice.lib.gui.elements.GUIButton;
import me.mrmaurice.lib.gui.elements.GUICoord;
import me.mrmaurice.lib.gui.elements.GUIPane;
import me.mrmaurice.lib.gui.enums.Rows;

@Getter
@Setter
public class SingleMenuGUI extends MenuGUI {

	private static final Comparator<GUIPane> sorter = Comparator.comparing(GUIPane::getPriority).reversed();
	
	private final List<GUIPane> panes = Lists.newArrayList();

	private Rows size = Rows.ONE;
	private Inventory inventory;

	public SingleMenuGUI() {
		this("", Rows.ONE);
	}

	public SingleMenuGUI(String title) {
		this(title, Rows.ONE);
	}

	public SingleMenuGUI(Rows size) {
		this("", size);
	}

	public SingleMenuGUI(String title, Rows size) {
		this.title = title;
		this.size = size;
	}

	public GUIButton getButton(int slot) {
		List<GUIPane> panes = getPanes();
		panes.sort(sorter);
		for (GUIPane pane : panes) {
			GUIButton butt = pane.getButton(slot);
			if (butt != null)
				return butt;
		}
		return null;
	}

	public void addPane(GUIPane pane) {
		panes.add(pane);
		panes.sort(sorter.reversed());
	}

	public GUIPane getFirstPaneAt(int x, int y) {
		return getFirstPaneAt(new GUICoord(x, y));
	}

	public GUIPane getFirstPaneAt(GUICoord coords) {
		List<GUIPane> panes = getPanes();
		panes.sort(sorter);
		return panes.stream().sorted(sorter).filter(p -> p.getCoords().equals(coords)).findFirst().orElse(null);
	}

	public List<GUIPane> getPanes() {
		return Lists.newArrayList(panes);
	}

	private void setContent() {
		panes.forEach(p -> p.display(inventory));
	}

	public void click(InventoryClickEvent event) {
		List<GUIPane> panes = getPanes();
		panes.sort(Comparator.comparing(GUIPane::getPriority).reversed());
		for (GUIPane pane : panes)
			if (pane.click(event))
				break;
	}

	public Inventory getInventory() {
		return inventory == null ? build() : inventory;
	}

	@Override
	public Inventory build() {
		title = title.length() > 32 ? title.substring(0, 31) : title;
		inventory = Bukkit.createInventory(this, size.toSlots(), title);
		setContent();
		return inventory;
	}

	@Override
	public void close(Player player) {
		player.closeInventory();
	}

	@Override
	public void open(Player player) {
		player.openInventory(build());
	}

}
