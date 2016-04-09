package com.snow.menu.Menus;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.snow.menu.Buttons.Basic.BEditMenu;
import com.snow.menu.Menu;
import com.snow.menu.MenuView;
import com.snow.menu.Menus.Attributes.EditableMenu;
import com.snow.menu.Menus.Attributes.EditableMenuHandler;
import com.snow.menu.Menus.Attributes.SaveableMenu;
import com.snow.menu.Menus.Attributes.SaveableMenuHandler;
import com.snow.menu.P;

/*
  Generic Editable Menu that creates an edit button that toggles the edit mode
 */
public class MEdit extends Menu implements EditableMenu, SaveableMenu {

	private EditableMenuHandler edit = new EditableMenuHandler(this);
	private SaveableMenuHandler save = new SaveableMenuHandler(this);
	private Set<UUID> editing = new HashSet<UUID>();

	public MEdit(String name, int size) {
		super(name, size);
		addButton(new BEditMenu(), BEditMenu.getDefaultSlot());
	}

	@Override
	public EditableMenuHandler getEditHandler() {
		return edit;
	}

	protected void setEditHandler(EditableMenuHandler editHandler) {
		edit = editHandler;
	}

	@Override
	public SaveableMenuHandler getSaveHandler() {
		return save;
	}

	protected void setSaveHandler(SaveableMenuHandler saveHandler) {
		this.save = saveHandler;
	}

	@Override
	public void load() {
	}

	public void setEditing(UUID uuid, boolean editing) {
		if (editing) {
			this.editing.add(uuid);
		} else {
			this.editing.remove(uuid);
		}
	}

	public boolean isEditing(UUID uuid) {
		return editing.contains(uuid);
	}

	// If the Player can edit the Menu,
	// this is used for example to determine if the edit Button should be shown,
	// buttons can be moved or deleted, etc.
	// Returns true if the player can admin, by default
	@Override
	public boolean canEdit(Player player) {
		return canAdmin(player);
	}

	@Override
	public void clickOnSlot(InventoryClickEvent event, MenuView view) {
		if (editing.contains(event.getWhoClicked().getUniqueId())) {
			int slot = event.getSlot();
			if (getButtons()[slot] == null) {
				P.p.log("There should be no button!");
				return;
			}

			edit.clickOnSlot(event, view);
		} else {
			super.clickOnSlot(event, view);
		}
	}

	@Override
	public void clickOnPlayerInv(InventoryClickEvent event, MenuView view) {
		if (editing.contains(event.getWhoClicked().getUniqueId())) {
			edit.clickOnPlayerInv(event, view);
		} else {
			super.clickOnPlayerInv(event, view);
		}
	}

	@Override
	public void clickOnEmptySlot(InventoryClickEvent event, MenuView view) {
		if (editing.contains(event.getWhoClicked().getUniqueId())) {
			edit.clickOnEmptySlot(event, view);
		} else {
			super.clickOnEmptySlot(event, view);
		}
	}

	@Override
	public void closingMenu(Player player, MenuView view, MenuView target) {
		if (editing.contains(player.getUniqueId())) {
			edit.closingMenu(player, view);
			if (target == null || (view != target && !(target.getMenu() instanceof MItemSelect))) {
				setEditing(player.getUniqueId(), false);
			}
		} else {
			super.closingMenu(player, view, target);
		}
	}

	@Override
	public void openingMenu(Player player, MenuView view, boolean fresh, MenuView old) {
		if (!fresh && old != null && old.getMenu() instanceof MItemSelect) {
			view.update(player);
		}
		super.openingMenu(player, view, fresh, old);
	}
}
