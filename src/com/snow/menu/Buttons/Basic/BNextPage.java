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

public class BNextPage extends Button {

	public BNextPage(PagedMenuHandler pages) {
		super(Material.WHITE_BANNER, "Nächste Seite");
		setAmount(pages.getPageIndex() + 2);
		setLoreLine(0, "Seite " + (pages.getPageIndex() + 1) + "/" + pages.getNumPages());
	}

	// Add This Button to the default slot in the menu
	public BNextPage addToMenu(Menu menu) {
		menu.addButton(this, getDefaultRow(), getDefaultColumn());
		return this;
	}

	public static int getDefaultColumn() {
		return 8;
	}

	public static int getDefaultRow() {
		return 0;
	}

	@Override
	public void click(InventoryClickEvent event, MenuView view) {
		if (view.getMenu() instanceof PagedMenu) {
			PagedMenuHandler pages = ((PagedMenu) view.getMenu()).getMenuPages();
			if (pages.hasNext()) {
				PagedMenu menu = pages.getNext().getMenu();
				MenuView cached = PagedMenuHandler.getCached(event.getWhoClicked().getUniqueId(), menu);
				if (cached != null) {
					cached.showAgain((Player) event.getWhoClicked(), view);
					//P.p.log("could show cached menu while going forward!");
				} else {
					menu.show((Player) event.getWhoClicked());
				}
			}
		}
	}
}
