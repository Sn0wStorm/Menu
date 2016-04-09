package com.snow.menu.Menus.Attributes;


import org.bukkit.entity.Player;

import com.snow.menu.IMenu;
import com.snow.menu.MenuView;

public interface PagedMenu extends IMenu {

	PagedMenuHandler getMenuPages();
	void setMenuPages(PagedMenuHandler p);

	/*
	  IMPORTANT!!!:
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

}
