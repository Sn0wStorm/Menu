package com.snow.menu.Buttons.Test;

import com.snow.menu.Buttons.Tools.Selector;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.Button;
import com.snow.menu.MenuView;
import com.snow.menu.Menus.MItemSelect;

import java.util.UUID;

public class BTestItemSelect extends Button {

	public BTestItemSelect(Material type) {
		super(type);
	}

	public BTestItemSelect(Material type, String name) {
		super(type, name);
	}

	public BTestItemSelect(Material type, String name, String... lore) {
		super(type, name, lore);
	}

	public BTestItemSelect(ItemStack item) {
		super(item);
	}

	@Override
	public void onClick(InventoryClickEvent event, MenuView view) {
		MItemSelect.allItems.show((Player) event.getWhoClicked(), new Selector() {
			@Override
			public void selected(UUID player, Button button) {
				Bukkit.getPlayer(player).sendMessage("selected: " + button.getType().toString().toLowerCase().replaceAll("_", " "));
			}
		});
	}
}
