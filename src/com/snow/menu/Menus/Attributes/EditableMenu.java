package com.snow.menu.Menus.Attributes;

import org.bukkit.entity.Player;

import com.snow.menu.IMenu;

public interface EditableMenu extends IMenu {

	EditableMenuHandler getEditHandler();
	//public void setEditHandler(EditableMenuHandler edit);

	// If the Player can edit the Menu,
	// this is used for example to determine if the edit Button should be shown,
	// buttons can be moved or deleted, etc.
	boolean canEdit(Player player);

}
