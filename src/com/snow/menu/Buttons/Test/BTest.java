package com.snow.menu.Buttons.Test;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

import com.snow.menu.Buttons.Button;

public class BTest extends Button {

	public BTest(Material type, String name) {
		super(type, name);
		List<String> lore = new ArrayList<>();
		lore.add("§7Test Lore");
		lore.add("§7A Menu Item!");
		setLore(lore);
		setClickFunction((p, v) -> p.sendMessage("You clicked meee :D"));
	}
}
