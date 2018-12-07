package com.snow.menu.Menus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.Basic.BNextPage;
import com.snow.menu.Buttons.Basic.BPrevPage;
import com.snow.menu.Buttons.Button;
import com.snow.menu.Buttons.Tools.BItemSelect;
import com.snow.menu.Buttons.Tools.Selector;
import com.snow.menu.Menu;
import com.snow.menu.MenuView;
import com.snow.menu.Menus.Attributes.NoBackMenu;
import com.snow.menu.Menus.Attributes.PagedMenu;
import com.snow.menu.Menus.Attributes.PagedMenuHandler;
import com.snow.menu.Util.NMSUtil;

public class MItemSelect extends Menu implements PagedMenu, NoBackMenu {

	public static MItemSelect allItems;
	public PagedMenuHandler menuPages;

	public MItemSelect() {
		super("Item Auswählen", 6);
	}

	public static MItemSelect initAll() {
		int slot = 9;
		MItemSelect head = new MItemSelect();
		List<MItemSelect> pages = new ArrayList<MItemSelect>();
		MItemSelect currentPage = head;
		pages.add(head);
		List<ItemStack> all = NMSUtil.getAllItems();

		for (ItemStack i : all) {
			//ItemStack i = new ItemStack(mat);
			if (i != null) {
				currentPage.addButton(new BItemSelect(i), slot);
				slot++;
				if (slot > 53) {
					slot = 9;
					currentPage = new MItemSelect();
					pages.add(currentPage);
				}
			}
		}
		PagedMenuHandler.init(pages.toArray(new MItemSelect[pages.size()]));
		allItems = head;
		return head;
	}

	// useSelector() Method needs to be called BEFORE calling this!
	// Instead, you can use show(Player, Selector)
	@Override
	public MenuView show(Player player) {
		if (BItemSelect.getSelector(player.getUniqueId()) == null) {
			throw new IllegalStateException("No Selector Specified!");
		}

		Button b = getButton(0, 8);
		if (b != null && b instanceof BNextPage) {
			b.setLoreLine(0, "Seite " + (menuPages.getPageIndex() + 1) + "/" + menuPages.getNumPages());
		}
		b = getButton(0, 7);
		if (b != null && b instanceof BPrevPage) {
			b.setLoreLine(0, "Seite " + (menuPages.getPageIndex() + 1) + "/" + menuPages.getNumPages());
		}
		MenuView view = super.show(player);
		PagedMenuHandler.cache(player.getUniqueId(), view);
		return view;
	}

	public MenuView show(Player player, Selector selector) {
		BItemSelect.useSelector(player.getUniqueId(), selector);
		return show(player);
	}

	@Override
	public void closingMenu(Player player, MenuView view, MenuView target) {
		super.closingMenu(player, view, target);
		if (target == null || !(target.getMenu() instanceof MItemSelect)) {
			removeSelector(player.getUniqueId());
			PagedMenuHandler.clearCache(player);
		}
	}

	@Override
	public PagedMenuHandler getMenuPages() {
		return menuPages;
	}

	@Override
	public void setMenuPages(PagedMenuHandler p) {
		menuPages = p;
	}

	public static void useSelector(UUID player, Selector selector) {
		BItemSelect.useSelector(player, selector);
	}

	public static void removeSelector(UUID player) {
		BItemSelect.removeSelector(player);
	}

	public static Selector getSelector(UUID player) {
		return BItemSelect.getSelector(player);
	}
}
