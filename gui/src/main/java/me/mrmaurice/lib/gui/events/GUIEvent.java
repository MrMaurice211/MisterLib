package me.mrmaurice.lib.gui.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import me.mrmaurice.lib.gui.GUI;

@Getter
public class GUIEvent extends Event {

	private GUI<?> affectedGUI;
	private Player responsible;

	public GUIEvent(Player player, GUI<?> gui) {
		this.responsible = player;
		this.affectedGUI = gui;
	}

	private static final HandlerList handlers = new HandlerList();

	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

}
