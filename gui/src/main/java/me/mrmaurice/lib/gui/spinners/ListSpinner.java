package me.mrmaurice.lib.gui.spinners;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class ListSpinner extends AbstractSpinner<List<?>> {

	@Override
	public Inventory getInventory() {
		return null;
	}

	@Override
	public void click(InventoryClickEvent event) {
		
	}

	@Override
	public Inventory build() {
		return null;
	}

	@Override
	public void close(Player player) {
		
	}

	@Override
	public void open(Player player) {
		
	}

}
