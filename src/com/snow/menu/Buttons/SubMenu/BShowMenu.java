package com.snow.menu.Buttons.SubMenu;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.Button;
import com.snow.menu.Menu;
import com.snow.menu.MenuView;

/*
  Button that shows the set menu on click
 */

public class BShowMenu extends Button {

	private Menu menuToShow;

	public BShowMenu(Material type) {
		super(type);
	}

	public BShowMenu(Material type, short durability) {
		super(type, durability);
	}

	public BShowMenu(Material type, String name) {
		super(type, name);
	}

	public BShowMenu(Material type, short durability, String name) {
		super(type, durability, name);
	}

	public BShowMenu(Material type, String name, String... lore) {
		super(type, name, lore);
	}

	public BShowMenu(Material type, short durability, String name, String... lore) {
		super(type, durability, name, lore);
	}

	public BShowMenu(ItemStack item) {
		super(item);
	}

	public Menu getMenuToShow() {
		return menuToShow;
	}

	// Returns itself for easier creation
	public BShowMenu setMenuToShow(Menu menuToShow) {
		this.menuToShow = menuToShow;
		return this;
	}

	@Override
	public void click(InventoryClickEvent event, MenuView view) {
		if (menuToShow != null) {
			menuToShow.show(((Player) event.getWhoClicked()));
		}
	}
}
