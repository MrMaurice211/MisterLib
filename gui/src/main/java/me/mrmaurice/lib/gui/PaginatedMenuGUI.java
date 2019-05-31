package me.mrmaurice.lib.gui;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import com.google.common.collect.Lists;

import lombok.Getter;
import me.mrmaurice.lib.gui.elements.GUIButton;

@Getter
public class PaginatedMenuGUI extends GUI<Inventory> {

	private List<GUIButton> buttons = Lists.newLinkedList();

	@Override
	public Inventory build() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close(Player player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void open(Player player) {
		// TODO Auto-generated method stub
		
	}

}
