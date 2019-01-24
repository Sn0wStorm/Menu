package com.snow.menu.Buttons.Basic;

import com.snow.menu.Buttons.Attributes.ImmovableButton;
import com.snow.menu.Buttons.Button;
import com.snow.menu.Buttons.MultiStateButton;
import com.snow.menu.Buttons.Tools.Selector;
import com.snow.menu.Menu;
import com.snow.menu.MenuView;
import com.snow.menu.Menus.Attributes.EditableMenu;
import com.snow.menu.Menus.Attributes.SelectorMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class BAddButton extends MultiStateButton implements ImmovableButton, Selector {

	private SelectorMenu selectorMenu;

	public BAddButton(SelectorMenu selectorMenu) {
		this(Material.EGG);
		this.selectorMenu = selectorMenu;
	}

	public BAddButton(Material type) {
		this(type, "Knopf Hinzufügen");
	}

	public BAddButton(Material type, String name) {
		super(type, name);
		setItem(1, new BEmptyTopTile().getItem());
	}

	public BAddButton(Material type, String name, String... lore) {
		super(type, name, lore);
		setItem(1, new BEmptyTopTile().getItem());
	}

	public BAddButton(ItemStack... items) {
		super(items);
		setItem(1, new BEmptyTopTile().getItem());
	}

	@Override
	public int showState(Player player, MenuView view) {
		Menu menu = view.getMenu();
		if (menu instanceof EditableMenu) {
			return ((EditableMenu) menu).isEditing(player.getUniqueId()) ? 0 : 1;
		} else {
			return 1;
		}
	}

	public SelectorMenu getSelectorMenu() {
		return selectorMenu;
	}

	public void setSelectorMenu(SelectorMenu selectorMenu) {
		this.selectorMenu = selectorMenu;
	}

	@Override
	public void selected(UUID player, Button button) {
		currentMenu.addButton(button);
		button.update();
	}

	@Override
	public boolean canClick(Player player, MenuView view) {
		Menu menu = view.getMenu();
		if (menu instanceof EditableMenu) {
			return ((EditableMenu) menu).isEditing(player.getUniqueId());
		}
		return false;
	}

	@Override
	public void onClick(InventoryClickEvent event, MenuView view) {
		if (selectorMenu != null) {
			selectorMenu.show(((Player) event.getWhoClicked()), this);
		}
	}
}
