package com.snow.menu.Buttons.Attributes;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.IButton;
import com.snow.menu.MenuView;

public interface EditableButton extends IButton {

	EditableButtonHandler getEditHandler();
	void setEditHandler(EditableButtonHandler edit);

	// If the player can edit this button
	// returning true if he can admin the menu may be a good idea
	boolean canEdit(Player player);

	// The Button was edited by the Player
	// The new lore was already set to the button
	// The Button will be updated to the player automatically after this
	void wasEdited(Player player, List<String> newLore, List<String> oldLore);

	// Call: getEditHandler().updateEditable(Player);
	// in this Method
	// To update the Lore depending on if the player can edit the button
	@Override
	ItemStack getItem(Player player, MenuView view);
	/*
	  Sample:
	  getEditHandler().updateEditable(player);
	  return super.getItem(player, view);
	 */


	/*
	  Implementing Buttons will have to use the edithandlers edit() Method to open the editing window.
	  This editing windows still has to be openend by the player by right clicking,
	  So it is usefull to make it look like the button has to be double-right-clicked for editing
	  This is written to the lore by default.
	  The first click initiates the editing, the second actually opens the menu
	  Example Method to open the Editing Menu on double rightclick:

	  @Override
	  public void onClick(InventoryClickEvent event, MenuView view) {
	    if (event.getClick().equals(ClickType.RIGHT) || event.getClick().equals(ClickType.SHIFT_RIGHT)) {
			getEditHandler().edit((Player) event.getWhoClicked(), view);
	    }
	  }
	 */

}
