package com.snow.menu.Buttons.Basic;


import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.snow.menu.Buttons.Attributes.ImmovableButton;
import com.snow.menu.Buttons.Button;
import com.snow.menu.MenuView;
import com.snow.menu.P;

public class BHome extends Button implements ImmovableButton {

	public BHome() {
		super(Material.ACACIA_DOOR_ITEM, "Hauptmenü");
	}

	public static int getDefaultSlot() {
		return 2;
	}

	@Override
	public void click(InventoryClickEvent event, MenuView view) {
		if (P.p.mainMenu == view.getMenu()) return;
		if (view.getHomeMenu() != null) {
			view.getHomeMenu().showAgain((Player) event.getWhoClicked(), view);
		} else {
			P.p.mainMenu.show((Player) event.getWhoClicked());
		}
	}

}
