package com.snow.menu.Buttons.Basic;


import org.bukkit.DyeColor;

import com.snow.menu.Buttons.Attributes.ImmovableButton;

/*
  Empty Tile that is Black by Default
  It is Immovable and used for the Top Menu bar
 */

public class BEmptyTopTile extends BEmptyTile implements ImmovableButton {
	public BEmptyTopTile() {
		super(DyeColor.GRAY);
	}

	public BEmptyTopTile(DyeColor color) {
		super(color);
	}
}
