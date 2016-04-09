package com.snow.menu.Buttons.Basic;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.StatedButton;


public class BTips extends StatedButton {

	public BTips() {
		super(Material.TORCH, "Info");
	}

	public BTips(Material type) {
		super(type);
	}

	public BTips(Material type, short durability) {
		super(type, durability);
	}

	public BTips(Material type, String name) {
		super(type, name);
	}

	public BTips(Material type, short durability, String name) {
		super(type, durability, name);
	}

	public BTips(Material type, String name, String... lore) {
		super(type, name, lore);
	}

	public BTips(Material type, short durability, String name, String... lore) {
		super(type, durability, name, lore);
	}

	public BTips(ItemStack... items) {
		super(items);
	}
}
