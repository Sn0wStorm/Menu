package com.snow.menu.Buttons.Basic;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.MultiStateButton;

public abstract class BInfo extends MultiStateButton {

	public BInfo() {
		super(Material.TORCH, "Info");
	}

	public BInfo(Material type) {
		super(type);
	}

	public BInfo(Material type, String name) {
		super(type, name);
	}

	public BInfo(Material type, String name, String... lore) {
		super(type, name, lore);
	}

	public BInfo(ItemStack... items) {
		super(items);
	}

}
