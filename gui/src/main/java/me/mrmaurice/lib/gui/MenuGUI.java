package me.mrmaurice.lib.gui;

import java.util.function.Consumer;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class MenuGUI extends GUI<Inventory> implements InventoryHolder {

	private boolean clickAllowed;
	private Consumer<InventoryCloseEvent> closeListener;
	protected String title;
	
	public abstract void click(InventoryClickEvent event);
	
}
