package com.snow.menu.Menus.Attributes;

import com.snow.menu.Buttons.Tools.BItemSelect;
import com.snow.menu.Buttons.Tools.Selector;
import com.snow.menu.IMenu;
import com.snow.menu.MenuView;
import org.bukkit.entity.Player;

import java.util.UUID;


/*
  A Menu where a Player has to select an item
  When he clicks the item, the selected() Method in the registered
  ...Buttons.Tools.Selector interface is called
 */

public interface SelectorMenu extends IMenu {

	// Set a Selector, the selector could also be a lambda function
	static void useSelector(UUID player, Selector selector) {
		BItemSelect.useSelector(player, selector);
	}

	// Get the registered Selector
	static Selector getSelector(UUID player) {
		return BItemSelect.getSelector(player);
	}

	// Remove the registered Selector
	// Should be called in the onClosingMenu Method
	static void removeSelector(UUID player) {
		BItemSelect.removeSelector(player);
	}

	// Show the Menu to the Player and register the Selector beforehand
	MenuView show(Player player, Selector selector);


	/*
	  Example:

	// useSelector() Method needs to be called BEFORE calling this!
	// Instead, you can use show(Player, Selector)
	@Override
	public MenuView show(Player player) {
		if (SelectorMenu.getSelector(player.getUniqueId()) == null) {
			throw new IllegalStateException("No Selector Specified!");
		}
		return super.show(player);
	}

	@Override
	public MenuView show(Player player, Selector selector) {
		SelectorMenu.useSelector(player.getUniqueId(), selector);
		return show(player);
	}
	 */
}
