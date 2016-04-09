package com.snow.menu.Buttons.Basic;

import org.bukkit.DyeColor;
import org.bukkit.Material;

import com.snow.menu.Buttons.Button;

public class BEmptyTile extends Button {

	public BEmptyTile() {
		super(Material.STAINED_GLASS_PANE, (short) 7, " ");
	}

	public BEmptyTile(DyeColor color) {
		super(Material.STAINED_GLASS_PANE, color.getWoolData(), " ");
	}
}
