package me.mrmaurice.lib.gui.elements;

import java.util.function.Consumer;

import me.mrmaurice.lib.gui.GUI;
import me.mrmaurice.lib.gui.MenuGUI;
import me.mrmaurice.lib.gui.events.GUIEvent;

import org.bukkit.entity.Player;

public class GUIActions {

	private GUIActions() {}

	private static final String REASON = "close_reason";

	public static final Consumer<GUIEvent> RETURN = event -> {
		GUI<?> gui = event.getAffectedGUI();
		GUI<?> parent = gui.getParent();
		if (parent == null)
			return;
		Player player = event.getResponsible();
		gui.setData(REASON, "return");
//		gui.close(player);
//		Bukkit.getScheduler().runTaskLater(Guis.getInstance(), () -> gui.open(player), 1);
		parent.open(player);
	};

	public static final Consumer<GUIEvent> HARD_RETURN = event -> {
		GUI<?> gui = event.getAffectedGUI();
		GUI<?> parent = gui.getMainGUI();
		if (parent == null)
			return;
		Player player = event.getResponsible();
		gui.setData(REASON, "hard_return");
//		gui.close(player);
		parent.open(player);
	};

	public static final Consumer<GUIEvent> UPDATE = event -> {
		MenuGUI gui = (MenuGUI) event.getAffectedGUI();
		gui.build();
		event.getResponsible().updateInventory();
	};

	public static final Consumer<GUIEvent> CLOSE = event -> {
		GUI<?> gui = event.getAffectedGUI();
		gui.setData(REASON, "close");
		gui.close(event.getResponsible());
	};

	public static final Consumer<GUIEvent> HIDE = event -> {
		GUI<?> gui = event.getAffectedGUI();
		gui.setData(REASON, "hide");
		gui.close(event.getResponsible());
	};

	public static final ReturnAction OPEN_CHILD = new GUIActions().new ReturnAction();

	public class ReturnAction extends GUIActions {
		private ReturnAction() {}

		public Consumer<GUIEvent> get(String data) {
			return event -> {
				GUI<?> gui = event.getAffectedGUI();
				GUI<?> child = gui.getChild(data);
				if (child == null)
					throw new IllegalArgumentException("Child GUI " + data + " does not exists.");
				gui.setData(REASON, "hide");
				gui.close(event.getResponsible());
				child.open(event.getResponsible());
			};
		}

	}
}
