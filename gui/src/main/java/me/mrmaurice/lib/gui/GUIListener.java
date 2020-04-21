package me.mrmaurice.lib.gui;

import java.util.function.Consumer;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import me.mrmaurice.lib.utils.TaskUtil;

public class GUIListener implements Listener {

	@EventHandler(
			ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		Inventory inv = event.getClickedInventory();

		if (inv == null)
			return;

		if (event.getCurrentItem() == null)
			return;

		InventoryHolder holder = inv.getHolder();

		if (!(holder instanceof MenuGUI))
			return;

		MenuGUI gui = (MenuGUI) holder;

		event.setCancelled(!gui.isClickAllowed());

		gui.click(event);
	}

	@EventHandler(
			ignoreCancelled = true)
	public void onInventoryClose(InventoryCloseEvent event) {
		Inventory inv = event.getInventory();

		if (inv == null)
			return;

		InventoryHolder holder = inv.getHolder();

		if (!(holder instanceof MenuGUI))
			return;

		MenuGUI gui = (MenuGUI) holder;

		String data = gui.getData("close_reason");

		if (data != null)
			if (!data.equalsIgnoreCase("close"))
				return;

		if (!gui.isCloseable()) {
			TaskUtil.runLater(() -> event.getPlayer().openInventory(gui.getInventory()), 1);
			return;
		}

		Consumer<InventoryCloseEvent> listener = gui.getCloseListener();

		if (listener != null)
			TaskUtil.runLater(() -> listener.accept(event), 1);

	}

}
