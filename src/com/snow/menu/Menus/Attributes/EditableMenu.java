package com.snow.menu.Menus.Attributes;

import org.bukkit.entity.Player;

import com.snow.menu.IMenu;

import java.util.UUID;

/*
  Menu that can be Edited by a Player
  Buttons may be Moved, added, removed etc.
  For Reference Implementation see EditMenu
 */
public interface EditableMenu extends IMenu {

	EditableMenuHandler getEditHandler();
	//public void setEditHandler(EditableMenuHandler edit);

	// If the Player can edit the Menu,
	// this is used for example to determine if the edit Button should be shown,
	// buttons can be moved or deleted, etc.
	boolean canEdit(Player player);

	// Set the editing State of this Menu
	void setEditing(UUID uuid, boolean editing);

	// Get the editing State of this Menu
	boolean isEditing(UUID uuid);

}
