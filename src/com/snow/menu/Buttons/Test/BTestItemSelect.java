package com.snow.menu.Buttons.Test;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.Button;
import com.snow.menu.MenuView;
import com.snow.menu.Menus.MItemSelect;

public class BTestItemSelect extends Button {

	public BTestItemSelect(Material type) {
		super(type);
	}

	public BTestItemSelect(Material type, short durability) {
		super(type, durability);
	}

	public BTestItemSelect(Material type, String name) {
		super(type, name);
	}

	public BTestItemSelect(Material type, short durability, String name) {
		super(type, durability, name);
	}

	public BTestItemSelect(Material type, String name, String... lore) {
		super(type, name, lore);
	}

	public BTestItemSelect(Material type, short durability, String name, String... lore) {
		super(type, durability, name, lore);
	}

	public BTestItemSelect(ItemStack item) {
		super(item);
	}

	@Override
	public void click(InventoryClickEvent event, MenuView view) {
		MItemSelect.allItems.show((Player) event.getWhoClicked());
	}
}
