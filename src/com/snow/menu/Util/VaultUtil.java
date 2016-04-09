package com.snow.menu.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import net.milkbowl.vault.item.ItemInfo;
import net.milkbowl.vault.item.Items;

public class VaultUtil {

	public static List<ItemStack> getAllItems() {
		List<ItemStack> all = new ArrayList<ItemStack>();
		for (ItemInfo info : Items.getItemList()) {
			all.add(new ItemStack(info.getType(), 1, info.getSubTypeId()));
		}
		return all;
	}
}
