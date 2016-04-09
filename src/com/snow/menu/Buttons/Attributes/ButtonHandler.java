package com.snow.menu.Buttons.Attributes;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.MenuView;

// Currently not implemented
public interface ButtonHandler {


	/* --- Methods to delegate to the Button --- */

	ItemStack getItem(Player player, MenuView view);
}
