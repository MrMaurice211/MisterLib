package me.mrmaurice.lib.gui.elements;

import java.util.function.Consumer;

import org.bukkit.inventory.Inventory;

import me.mrmaurice.lib.events.gui.GUIEvent;
import me.mrmaurice.lib.gui.GUI;
import me.mrmaurice.lib.gui.MenuGUI;

import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class GUIActions {

	private GUIActions() {}

	public static final Consumer<GUIEvent> RETURN = event -> {
		GUI<?> gui = event.getAffectedGUI();
		GUI<?> parent = gui.getParent();
		if (parent == null)
			return;
		Player player = event.getResponsible();
		gui.setData("close_reason", "return");
		gui.close(player);
		if (parent instanceof MenuGUI)
			player.openInventory((Inventory) gui.build());
		else
			((Conversation) gui.build()).begin();
	};

	public static final Consumer<GUIEvent> HARD_RETURN = event -> {
		GUI<?> gui = event.getAffectedGUI();
		GUI<?> parent = gui.getMainGUI();
		if (parent == null)
			return;
		Player player = event.getResponsible();
		gui.setData("close_reason", "hard_return");
		gui.close(player);
		if (parent instanceof MenuGUI)
			player.openInventory((Inventory) gui.build());
		else
			((Conversation) gui.build()).begin();
	};

	public static final Consumer<GUIEvent> UPDATE = event -> {
		MenuGUI gui = (MenuGUI) event.getAffectedGUI();
		gui.build();
		event.getResponsible().updateInventory();
	};

	public static final Consumer<GUIEvent> CLOSE = event -> {
		GUI<?> gui = event.getAffectedGUI();
		gui.setData("close_reason", "close");
		gui.close(event.getResponsible());
	};

	public static final Consumer<GUIEvent> HIDE = event -> {
		GUI<?> gui = event.getAffectedGUI();
		gui.setData("close_reason", "hide");
		gui.close(event.getResponsible());
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
