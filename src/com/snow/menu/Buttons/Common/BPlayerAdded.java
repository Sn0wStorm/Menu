package com.snow.menu.Buttons.Common;

import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.Attributes.SaveableMetaButtonHandler;
import com.snow.menu.Buttons.Attributes.TakeableButton;
import com.snow.menu.Buttons.GlobalButton;

public class BPlayerAdded extends GlobalButton implements TakeableButton {

	public BPlayerAdded(ItemStack item) {
		super(item);
		setSaveHandler(new SaveableMetaButtonHandler(this));
	}

	// The Item that should go into the players inventory when taking this Button out of the Menu
	// !!! Return null if the Button should be taken out as is !!!
	@Override
	public ItemStack takeoutItem() {
		return null;
	}
}
