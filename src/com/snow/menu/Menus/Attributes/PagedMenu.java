package com.snow.menu.Menus.Attributes;


import com.snow.menu.Menu;
import org.bukkit.entity.Player;

import com.snow.menu.IMenu;
import com.snow.menu.MenuView;

public interface PagedMenu extends IMenu {

	PagedMenuHandler getMenuPages();
	void setMenuPages(PagedMenuHandler p);

	/*
	  ############   IMPORTANT!!!:   #################

	  Override closingMenu() and inside call the static close(-) method seen below

	  Or implement it yourself:

	  Implementing Class needs to call:
	  if (target == null || !(target.getMenu() instanceof MItemSelect)) {
			PagedMenuHandler.clearCache(player);
	  }
	  in:
	  closingMenu();
	  Replace MItemSelect with the implementing Classes Type
	 */

	@Override
	void closingMenu(Player player, MenuView view, MenuView target);

	static void close(Player player, MenuView view, MenuView target) {
		if (target == null) {
			// If there is no target, so we are just closing the Menu
			// Clear the Menu Page Cache
			PagedMenuHandler.clearCache(player);
		} else if (pagesOfSameMenu((PagedMenu) view.getMenu(), target)) {
			// If the Target is a Page of this same Menu
			// Set the Back Menu of the target to the Back menu of this view
			// Which should then be the previous menu
			if (!view.getMenu().isNoBack()) {
				target.setBackMenu(view.getBackMenu());
			}
		} else {
			// If the target is a different menu
			// Clear the Menu Page Cache
			PagedMenuHandler.clearCache(player);
		}
	}

	static boolean pagesOfSameMenu(PagedMenu one, MenuView two) {
		return one.getMenuPages().isPageOfThisMenu(two.getMenu());
	}

}
