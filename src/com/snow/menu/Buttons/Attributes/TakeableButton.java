package com.snow.menu.Buttons.Attributes;

import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.IButton;

/*
  Having a Button implement this interface makes the Player able to take this Button out of the Menu and put in the Players inventory
  Only applicable to EditableMenu with the EditableMenuHandler set to realButtons
  Useful for Buttons that resemble Simple Items to be taken by the Player
  Also used by Item-Buttons that were placed into the Menu by the Player
 */
public interface TakeableButton extends IButton {

	// The Item that should go into the players inventory when taking this Button out of the Menu
	// !!! Return null if the Button should be taken out as is !!!
	 ItemStack takeoutItem();
}
