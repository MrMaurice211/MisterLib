package me.mrmaurice.lib.gui.elements;

import java.util.function.Consumer;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import me.mrmaurice.lib.gui.GUI;
import me.mrmaurice.lib.gui.MenuGUI;

import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIActions {

	private GUIActions() {}

	public static final Consumer<InventoryClickEvent> RETURN = event -> {
		MenuGUI gui = (MenuGUI) event.getInventory().getHolder();
		GUI<Inventory> parent = gui.getParent();
		if (parent == null)
			return;
		gui.setData("close_reason", "return");
		event.getWhoClicked().closeInventory();
		event.getWhoClicked().openInventory(parent.build());
	};

	public static final Consumer<InventoryClickEvent> HARD_RETURN = event -> {
		MenuGUI gui = (MenuGUI) event.getInventory().getHolder();
		GUI<Inventory> parent = gui.getMainGUI();
		if (parent == null)
			return;
		gui.setData("close_reason", "hard_return");
		event.getWhoClicked().closeInventory();
		event.getWhoClicked().openInventory(parent.build());
	};

	public static final Consumer<InventoryClickEvent> UPDATE = event -> {
		MenuGUI gui = (MenuGUI) event.getInventory().getHolder();
		gui.build();
		((Player) event.getWhoClicked()).updateInventory();
	};

	public static final Consumer<InventoryClickEvent> CLOSE = event -> {
		MenuGUI gui = (MenuGUI) event.getInventory().getHolder();
		gui.setData("close_reason", "close");
		event.getWhoClicked().closeInventory();
	};

	public static final Consumer<InventoryClickEvent> HIDE = event -> {
		MenuGUI gui = (MenuGUI) event.getInventory().getHolder();
		gui.setData("close_reason", "hide");
		event.getWhoClicked().closeInventory();
	};

	public static final ReturnAction OPEN_CHILD = new GUIActions().new ReturnAction();

	public class ReturnAction extends GUIActions {
		private ReturnAction() {}

		public Consumer<InventoryClickEvent> get(String data) {
			return event -> {
				MenuGUI gui = (MenuGUI) event.getInventory().getHolder();
				GUI<Inventory> child = gui.getChild(data);
				if (child == null)
					throw new IllegalArgumentException("Child GUI " + data + " does not exists.");
				gui.setData("close_reason", "hide");
				event.getWhoClicked().closeInventory();
				event.getWhoClicked().openInventory(child.build());
			};
		}

	}
}
