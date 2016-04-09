package com.snow.menu.Buttons;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.Attributes.EditableButton;
import com.snow.menu.Buttons.Attributes.EditableButtonHandler;
import com.snow.menu.MenuView;
import com.snow.menu.P;

/*
  Generic Global Button that is Editable by OP Players
 */

public class GlobalEditableButton extends GlobalButton implements EditableButton {

	private EditableButtonHandler edit = new EditableButtonHandler(this);

	public GlobalEditableButton(Material type) {
		super(type);
	}

	public GlobalEditableButton(Material type, short durability) {
		super(type, durability);
	}

	public GlobalEditableButton(Material type, String name) {
		super(type, name);
	}

	public GlobalEditableButton(Material type, short durability, String name) {
		super(type, durability, name);
	}

	public GlobalEditableButton(Material type, String name, String... lore) {
		super(type, name, lore);
	}

	public GlobalEditableButton(Material type, short durability, String name, String... lore) {
		super(type, durability, name, lore);
	}

	public GlobalEditableButton(ItemStack item) {
		super(item);
	}

	@Override
	public EditableButtonHandler getEditHandler() {
		return edit;
	}

	@Override
	public void setEditHandler(EditableButtonHandler edit) {
		this.edit = edit;
	}

	// If the player can edit this button
	// returns true if he can admin the menu, by default
	@Override
	public boolean canEdit(Player player) {
		return currentMenu.canAdmin(player);
	}

	// The Button was edited, we should save it
	@Override
	public void wasEdited(Player player, List<String> newLore, List<String> oldLore) {
		P.p.log("saving editable Global Button");
		save();
	}

	// Init The Buttons editor and the lore with edit text and existing lore
	public GlobalEditableButton init(boolean nameEditable) {
		edit.nameEditable = nameEditable;
		edit.initFromLore();
		edit.updateLore();
		return this;
	}

	@Override
	public ItemStack getItem(Player player, MenuView view) {
		getEditHandler().updateEditable(player);
		return super.getItem(player, view);
	}

	// Allows editing on Double Rightclick if canEdit() returns true
	// Can be overridden to add more advanced editing etc.
	@Override
	public void click(InventoryClickEvent event, MenuView view) {
		if (canEdit(((Player) event.getWhoClicked())) && (event.getClick().equals(ClickType.RIGHT) || event.getClick().equals(ClickType.SHIFT_RIGHT))) {
			edit.edit((Player) event.getWhoClicked(), view);
		}
	}
}
