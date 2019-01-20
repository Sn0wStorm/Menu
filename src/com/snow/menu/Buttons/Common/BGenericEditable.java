package com.snow.menu.Buttons.Common;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.Attributes.EditableButton;
import com.snow.menu.Buttons.Attributes.EditableButtonHandler;
import com.snow.menu.Buttons.Button;
import com.snow.menu.MenuView;

/*
  Useless Button that can be edited and the changes are visible to everyone
  Does no permission check, saving, loading or anything else
 */

public class BGenericEditable extends Button implements EditableButton {

	private EditableButtonHandler editHandler = new EditableButtonHandler(this);

	public BGenericEditable(Material type) {
		super(type);
	}

	public BGenericEditable(Material type, String name) {
		super(type, name);
	}

	public BGenericEditable(Material type, String name, String... lore) {
		super(type, name, lore);
	}

	public BGenericEditable(ItemStack item) {
		super(item);
	}

	@Override
	public void click(InventoryClickEvent event, MenuView view) {
		if (event.getClick().equals(ClickType.RIGHT) || event.getClick().equals(ClickType.SHIFT_RIGHT)) {
			editHandler.edit((Player) event.getWhoClicked(), view);
		}
	}

	@Override
	public boolean canEdit(Player player) {
		return currentMenu.canAdmin(player);
	}

	@Override
	public void wasEdited(Player player, List<String> newLore, List<String> oldLore) {
	}

	@Override
	public EditableButtonHandler getEditHandler() {
		return editHandler;
	}

	@Override
	public void setEditHandler(EditableButtonHandler edit) {
		editHandler = edit;
	}
}
