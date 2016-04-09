package com.snow.menu.Buttons.Common;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.Attributes.TakeableButton;
import com.snow.menu.Buttons.Button;

/**
 * Simple Button that represents an ItemStack
 * Can be taken from the Menu
 * No Saving, etc. Just for a Player to take out of the Menu
 */
public class TakeoutButton extends Button implements TakeableButton {

	public TakeoutButton(Material type) {
		super(type);
	}

	public TakeoutButton(Material type, short durability) {
		super(type, durability);
	}

	public TakeoutButton(Material type, String name) {
		super(type, name);
	}

	public TakeoutButton(Material type, short durability, String name) {
		super(type, durability, name);
	}

	public TakeoutButton(Material type, String name, String... lore) {
		super(type, name, lore);
	}

	public TakeoutButton(Material type, short durability, String name, String... lore) {
		super(type, durability, name, lore);
	}

	public TakeoutButton(ItemStack item) {
		super(item);
	}

	@Override
	public ItemStack takeoutItem() {
		return null;
	}
}
