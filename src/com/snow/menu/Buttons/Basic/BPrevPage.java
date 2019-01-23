package com.snow.menu.Buttons.Basic;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.snow.menu.Buttons.Button;
import com.snow.menu.Menu;
import com.snow.menu.MenuView;
import com.snow.menu.Menus.Attributes.PagedMenuHandler;
import com.snow.menu.Menus.Attributes.PagedMenu;
import com.snow.menu.P;

public class BPrevPage extends Button {

	public BPrevPage(PagedMenuHandler pages) {
		super(Material.WHITE_BANNER, "Vorherige Seite");
		setAmount(pages.getPageIndex());
		setLoreLine(0, "Seite " + (pages.getPageIndex() + 1) + "/" + pages.getNumPages());
	}

	// Add This Button to the default slot in the menu
	public BPrevPage addToMenu(Menu menu) {
		menu.addButton(this, getDefaultSlot());
		return this;
	}

	public static int getDefaultSlot() {
		return 7;
	}

	@Override
	public void onClick(InventoryClickEvent event, MenuView view) {
		if (view.getMenu() instanceof PagedMenu) {
			PagedMenuHandler pages = ((PagedMenu) view.getMenu()).getMenuPages();
			if (pages.hasPrev()) {
				PagedMenu menu = pages.getPrev().getMenu();
				MenuView cached = PagedMenuHandler.getCached(event.getWhoClicked().getUniqueId(), menu);
				if (cached != null) {
					cached.showAgain((Player) event.getWhoClicked(), view);
					//P.p.log("could show cached Menu while going back!");
				} else {
					menu.show((Player) event.getWhoClicked());
				}
			}
		}
	}

}
