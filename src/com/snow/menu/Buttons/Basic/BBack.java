package com.snow.menu.Buttons.Basic;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.Attributes.ImmovableButton;
import com.snow.menu.Buttons.StatedButton;
import com.snow.menu.MenuView;

public class BBack extends StatedButton implements ImmovableButton {
	public BBack() {
		super(Material.BIRCH_WOOD_STAIRS, "Zurück");
		setState(0, new BEmptyTopTile().getItem());
	}

	public static int getDefaultSlot() {
		return 1;
	}

	@Override
	public void click(InventoryClickEvent event, MenuView view) {
		if (view.getBackMenu() != null) {
			view.getBackMenu().showAgain((Player) event.getWhoClicked(), view);
		}
	}

	@Override
	public ItemStack getItem(Player player, MenuView view) {
		if (view.getBackMenu() != null) {
			return getItem();
		} else {
			return getState(0);
		}
	}
}
