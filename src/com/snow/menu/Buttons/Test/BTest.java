package com.snow.menu.Buttons.Test;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.snow.menu.Buttons.Button;
import com.snow.menu.MenuView;

public class BTest extends Button {

	public BTest(Material type, String name) {
		super(type, name);
		List<String> lore = new ArrayList<String>();
		lore.add("§7Test Lore");
		lore.add("§7A Menu Item!");
		setLore(lore);
	}

	@Override
	public void click(InventoryClickEvent event, MenuView view) {
		event.getWhoClicked().sendMessage("You clicked me :D");
	}
}
