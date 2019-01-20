package com.snow.menu.Buttons.Basic;


import com.snow.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.snow.menu.Buttons.Attributes.ImmovableButton;
import com.snow.menu.Buttons.Button;
import com.snow.menu.MenuView;
import com.snow.menu.P;

public class BHome extends Button implements ImmovableButton {

	public BHome() {
		super(Material.ACACIA_DOOR, "Hauptmenü");
	}

	public static int getDefaultSlot() {
		return 2;
	}

	@Override
	public boolean canSee(Player player, MenuView view) {
		return player.hasPermission("menu.home");
	}

	@Override
	public void onClick(InventoryClickEvent event, MenuView view) {
		Menu main = P.p.getPluginRegistry().getMainMenu();
		if (!event.getWhoClicked().hasPermission("menu.home")) return;
		if (view.getHomeMenu() != null) {
			if (view.getHomeMenu() != view) {
				view.getHomeMenu().showAgain((Player) event.getWhoClicked(), view);
			}
		} else if (main != null && main != view.getMenu()) {
			main.show((Player) event.getWhoClicked());
		}
	}

}
