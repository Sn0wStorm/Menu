package com.snow.menu.Menus.Tools;

import com.snow.menu.Buttons.Basic.BEmptyTile;
import com.snow.menu.Buttons.Button;
import com.snow.menu.Buttons.Tools.Selector;
import com.snow.menu.Menu;
import com.snow.menu.MenuView;
import com.snow.menu.Menus.Attributes.SelectorMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

public class SelectPlayerItemMenu extends Menu implements SelectorMenu {

	public SelectPlayerItemMenu() {
		this("Item Auswählen");
	}

	public SelectPlayerItemMenu(String name) {
		super(name, 2);
		showBasicButtons(true);
		for (int i = 9; i < 18; i++) {
			addButton(new BEmptyTile("Wähle eins deiner Items aus"), i);
		}
	}

	// useSelector() Method needs to be called BEFORE calling this!
	// Instead, you can use show(Player, Selector)
	@Override
	public MenuView show(Player player) {
		if (SelectorMenu.getSelector(player.getUniqueId()) == null) {
			throw new IllegalStateException("No Selector Specified!");
		}
		return super.show(player);
	}

	@Override
	public MenuView show(Player player, Selector selector) {
		SelectorMenu.useSelector(player.getUniqueId(), selector);
		return show(player);
	}

	@Override
	public void clickOnPlayerInv(InventoryClickEvent event, MenuView view) {
		super.clickOnPlayerInv(event, view);
		if (event.getSlotType() != InventoryType.SlotType.OUTSIDE) {
			if (event.isLeftClick()) {
				ItemStack currentItem = event.getCurrentItem();
				if (currentItem != null && currentItem.getType() != Material.AIR) {
					SelectorMenu.getSelector(event.getWhoClicked().getUniqueId()).selected(event.getWhoClicked().getUniqueId(), new Button(currentItem.getType()));
				}
			}
		}
	}

	@Override
	public void closingMenu(Player player, MenuView view, MenuView target) {
		super.closingMenu(player, view, target);
		SelectorMenu.removeSelector(player.getUniqueId());
	}
}
