package com.snow.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;

import com.snow.menu.Buttons.Button;
import com.snow.menu.Menus.Attributes.NoBackMenu;

  /*
    The open Menu of a single player
    should never be shown to multiple players
    holds the actual inventory and is returned for inventory.getHolder()
    a new instance is created for every menu session of a player
  */

public class MenuView implements InventoryHolder {

	private Inventory inv;
	private Menu menu;
	private MenuView back; // Menu that opens when pressing the Back Button
	private MenuView home; // Menu that opens when pressing the Home Button

	public MenuView(Menu menu) {
		this.menu = menu;
	}

	@Override
	public Inventory getInventory() {
		return inv;
	}

	protected void setInventory(Inventory inventory) {
		inv = inventory;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setBackMenu(MenuView backView) {
		if (backView != null) {
			while (backView.getMenu() instanceof NoBackMenu || backView.getMenu().isNoBack()) {
				backView = backView.getBackMenu();
				if (backView == null) {
					break;
				}
			}
		}
		back = backView;
	}

	public MenuView getBackMenu() {
		return back;
	}

	public MenuView getHomeMenu() {
		return home;
	}

	public void setHomeMenu(MenuView homeView) {
		home = homeView;
	}

	// Show this prepared Menu to a player again
	// Can only be shown to one player at a time!
	// Should only be used to show the player a menu again, that he was already on (back button, etc)
	// The View the player is coming from should be given as old, if the player has no menu opened, put null
	public void showAgain(Player player, MenuView old) {
		if (old != null) {
			MenuListener.storeClosing(player.getUniqueId(), old);
			old.getMenu().closingMenu(player, old, this);
			old.closed();
		}
		menu.listOpenView(this);
		menu.openingMenu(player, this, false, old);
		player.openInventory(inv);
	}

	// Update this view for the player, updating all items in the inventory
	public void update(Player player) {
		Button[] buttons = getMenu().getButtons();
		for (int i = 0; i < inv.getSize() && i < buttons.length; i++) {
			if (buttons[i] == null) {
				inv.setItem(i, null);
				continue;
			}
			if (buttons[i].canSee(player, this)) {
				inv.setItem(i, buttons[i].getItem(player, this));
			} else {
				inv.setItem(i, null);
			}
		}
	}

	protected void closed() {
		menu.listCloseView(this);
	}

	public static MenuView getOpenView(Player player) {
		InventoryView inv = player.getOpenInventory();
		if (inv != null && inv.getTopInventory() != null) {
			InventoryHolder holder = inv.getTopInventory().getHolder();
			if (holder != null && holder instanceof MenuView) {
				return ((MenuView) holder);
			}
		}
		return null;
	}
}
