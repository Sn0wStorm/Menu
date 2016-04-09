package com.snow.menu.Buttons.Basic;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.StatedButton;

public class BInfo extends StatedButton {
	public BInfo(Material type) {
		super(type);
	}

	public BInfo(Material type, short durability) {
		super(type, durability);
	}

	public BInfo(Material type, String name) {
		super(type, name);
	}

	public BInfo(Material type, short durability, String name) {
		super(type, durability, name);
	}

	public BInfo(Material type, String name, String... lore) {
		super(type, name, lore);
	}

	public BInfo(Material type, short durability, String name, String... lore) {
		super(type, durability, name, lore);
	}

	public BInfo(ItemStack... items) {
		super(items);
	}

}
