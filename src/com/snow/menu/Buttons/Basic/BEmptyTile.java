package com.snow.menu.Buttons.Basic;

import org.bukkit.DyeColor;
import org.bukkit.Material;

import com.snow.menu.Buttons.Button;

public class BEmptyTile extends Button {

	public BEmptyTile() {
		this(" ");
	}

	public BEmptyTile(DyeColor color) {
		this(color, " ");

	}

	public BEmptyTile(String name) {
		super(Material.LIGHT_GRAY_STAINED_GLASS_PANE, name);
	}

	public BEmptyTile(DyeColor color, String name) {
		this(name);
		for (Material m : Material.values()) {
			String n = m.name();
			if (n.endsWith("STAINED_GLASS_PANE") && n.startsWith(color.name())) {
				setType(m);
				break;
			}
		}
	}
}
