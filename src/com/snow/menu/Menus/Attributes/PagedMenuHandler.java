package com.snow.menu.Menus.Attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.snow.menu.Buttons.Basic.BNextPage;
import com.snow.menu.Buttons.Basic.BPrevPage;
import com.snow.menu.MenuView;
import com.snow.menu.P;

public class PagedMenuHandler {
	private static Map<UUID, Set<MenuView>> cache = new HashMap<UUID, Set<MenuView>>();
	private List<PagedMenuHandler> pages;
	private PagedMenu menu;
	private int index;

	public static void init(PagedMenu... menus) {
		List<PagedMenuHandler> list = new ArrayList<PagedMenuHandler>();
		for (int i = 0; i < menus.length; i++) {
			PagedMenu menu = menus[i];
			PagedMenuHandler h = new PagedMenuHandler(list, menu, i);
			list.add(h);
			menu.setMenuPages(h);
			menu.showBasicButtons(true);
			if (i < menus.length - 1) {
				menu.addButton(new BNextPage(h), BNextPage.getDefaultSlot());
			}
			if (i > 0) {
				menu.addButton(new BPrevPage(h), BPrevPage.getDefaultSlot());
			}
		}
	}

	private PagedMenuHandler(List<PagedMenuHandler> list, PagedMenu menu, int index) {
		this.pages = list;
		this.menu = menu;
		this.index = index;
	}

	public boolean hasNext() {
		return (pages.size() > index + 1);
	}

	public PagedMenuHandler getNext() {
		if (hasNext()) {
			return pages.get(index + 1);
		}
		return null;
	}

	public PagedMenu getNextMenu() {
		if (hasNext()) {
			return pages.get(index + 1).menu;
		}
		return null;
	}

	public boolean hasPrev() {
		return (index - 1 >= 0);
	}

	public PagedMenuHandler getPrev() {
		if (hasPrev()) {
			return pages.get(index - 1);
		}
		return null;
	}

	public PagedMenu getMenu() {
		return menu;
	}

	public static MenuView getCached(UUID uuid, PagedMenu menu) {
		Set<MenuView> set = cache.get(uuid);
		if (set != null) {
			for (MenuView view : set) {
				if (view.getMenu() == menu) {
					return view;
				}
			}
		}
		return null;
	}

	public static void cache(UUID uuid, MenuView view) {
		if (cache.containsKey(uuid)) {
			cache.get(uuid).add(view);
		} else {
			Set<MenuView> set = new HashSet<MenuView>();
			set.add(view);
			cache.put(uuid, set);
		}
	}

	public static void clearCache(Player player) {
		cache.remove(player.getUniqueId());
		P.p.log("Cache Cleared!");
	}

	public PagedMenuHandler getPage(int index) {
		if (index >= 0 && index < pages.size()) {
			return pages.get(index);
		}
		return null;
	}

	public int getNumPages() {
		return pages.size();
	}

	public int getPageIndex() {
		return index;
	}

	public void addPage(PagedMenu... menus) {
		for (int i = 0; i < menus.length; i++) {
			PagedMenu pmenu = menus[i];
			PagedMenuHandler h = new PagedMenuHandler(pages, pmenu, i + index + 1);
			pages.add(h);
			pmenu.setMenuPages(h);
		}
		updateIndex(index);
	}

	public void removePage() {
		pages.remove(index);
		updateIndex(index);
	}

	public void removePage(int index) {
		if (index >= 0 && pages.size() > index) {
			pages.remove(index);
			updateIndex(index);
		}
	}

	private void updateIndex(int startIndex) {
		int size = pages.size();
		while (size > startIndex) {
			pages.get(startIndex).index = startIndex;
			startIndex++;
		}
	}
}
