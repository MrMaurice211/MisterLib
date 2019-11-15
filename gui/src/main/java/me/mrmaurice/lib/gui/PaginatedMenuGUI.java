package me.mrmaurice.lib.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import lombok.Getter;
import me.mrmaurice.lib.gui.elements.GUIButton;
import me.mrmaurice.lib.gui.elements.GUIToolbar;
import me.mrmaurice.lib.utils.Paginator;

@Getter
public class PaginatedMenuGUI extends MenuGUI {

	private Paginator<GUIButton> paginator = new Paginator<GUIButton>(45);
	private GUIToolbar toolbar;

	public PaginatedMenuGUI() {
		this("");
	}

	public PaginatedMenuGUI(String title) {
		this.title = title;
	}

	@Override
	public Inventory build() {
		title = title.length() > 32 ? title.substring(0, 31) : title;
		int size = getTotalSize();
		Inventory inv = Bukkit.createInventory(this, size, title);
		List<GUIButton> butts = paginator.getPage();
		butts.forEach(butt -> {
			if (butt != null)
				inv.addItem(butt.toItemStack());
		});
		if (toolbar != null) {
			int last = getUsableSize();
			for (int i = 0; i < 9; i++) {
				GUIButton butt = toolbar.get(i);
				if (butt != null)
					inv.setItem(last + i, butt.toItemStack());
			}
		}
		return inv;
	}

	@Override
	public void close(Player player) {
		player.closeInventory();
	}

	@Override
	public void open(Player player) {
		player.openInventory(build());
	}

	private int getTotalSize() {
		int pageSize = getUsableSize();
		if (toolbar != null)
			pageSize += 9;
		return pageSize;
	}

	private int getUsableSize() {
		if (paginator.getTotalPages() > 1)
			return 45;
		return roundSize(paginator.getPage().size());
	}

	@Override
	public Inventory getInventory() {
		return build();
	}

	private int roundSize(int size) {
		if (size <= 0)
			size = 9;
		double d = Math.ceil(size / 9.0);
		if (d <= 0.0)
			d = 1.0;
		return (int) (d * 9);
	}

	@Override
	public void click(InventoryClickEvent event) {
		int slot = event.getSlot();
		int size = getUsableSize();

		if (toolbar != null && slot > size - 1) {
			GUIButton butt = toolbar.get(slot - size);
			if (butt != null)
				butt.run(event);
			return;
		}

		List<GUIButton> buttons = paginator.getPage();
		GUIButton butt = buttons.get(event.getSlot());
		if (butt != null)
			butt.run(event);
	}

}
