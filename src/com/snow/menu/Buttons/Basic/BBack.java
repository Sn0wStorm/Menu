package com.snow.menu.Buttons.Basic;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.snow.menu.Buttons.Attributes.ImmovableButton;
import com.snow.menu.Buttons.MultiStateButton;
import com.snow.menu.MenuView;

public class BBack extends MultiStateButton implements ImmovableButton {
	public BBack() {
		super(Material.BIRCH_STAIRS, "Zurück");
		setItem(1, new BEmptyTopTile().getItem());
	}

	public static int getDefaultSlot() {
		return 1;
	}

	@Override
	public void onClick(InventoryClickEvent event, MenuView view) {
		if (view.getBackMenu() != null) {
			view.getBackMenu().showAgain((Player) event.getWhoClicked(), view);
		}
	}

	@Override
	public int showState(Player player, MenuView view) {
		if (view.getBackMenu() != null) {
			return 0;
		} else {
			return 1;
		}
	}
}
