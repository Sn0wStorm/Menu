package com.snow.menu.Buttons.Attributes;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.snow.menu.Buttons.GlobalButton;

/*
  SaveableGlobalButtonHandler that saves the ItemStacks Meta in the extra data
 */
public class SaveableMetaButtonHandler extends SaveableGlobalButtonHandler {
	public SaveableMetaButtonHandler(GlobalButton b) {
		super(b);
	}

	@Override
	public boolean saveStart() {
		ItemStack item = getButton().getItem();
		if (item != null && item.hasItemMeta())  {
			getExtra().set("ItemMeta", item.getItemMeta());
		}
		return super.saveStart();
	}

	@Override
	public void saveDone() {
		if (getExtra() == null || !getExtra().contains("ItemMeta")) {
			return;
		}
		ItemStack item = getButton().getItem();
		if (item != null) {
			Object o = getExtra().get("ItemMeta", null);
			if (o != null && o instanceof ItemMeta) {
				item.setItemMeta(((ItemMeta) o));
			}
		}
	}
}
