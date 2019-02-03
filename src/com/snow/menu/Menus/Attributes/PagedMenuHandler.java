package com.snow.menu.Menus.Attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.snow.menu.Buttons.Basic.BEmptyTopTile;
import com.snow.menu.Buttons.Button;
import com.snow.menu.Menu;
import com.snow.menu.P;
import org.bukkit.entity.Player;

import com.snow.menu.Buttons.Basic.BNextPage;
import com.snow.menu.Buttons.Basic.BPrevPage;
import com.snow.menu.MenuView;

public class PagedMenuHandler<M extends PagedMenu> {
	private static Map<UUID, Set<MenuView>> cache = new HashMap<>();
	private List<PagedMenuHandler<M>> pages;
	private M menu;
	private int index;

	public static <M extends PagedMenu> void init(M... menus) {
		List<PagedMenuHandler<M>> list = new ArrayList<>();
		for (int i = 0; i < menus.length; i++) {
			M menu = menus[i];
			PagedMenuHandler<M> h = new PagedMenuHandler<>(list, menu, i);
			list.add(h);
			menu.setMenuPages(h);
			menu.showBasicButtons(true);
			if (i < menus.length - 1) {
				menu.addButton(new BNextPage(i, menus.length), BNextPage.getDefaultSlot());
			}
			if (i > 0) {
				menu.addButton(new BPrevPage(i, menus.length), BPrevPage.getDefaultSlot());
			}
		}
	}

	private PagedMenuHandler(List<PagedMenuHandler<M>> list, M menu, int index) {
		this.pages = list;
		this.menu = menu;
		this.index = index;
	}

	public M getMenu() {
		return menu;
	}

	public void forAllPages(Consumer<M> action) {
		for (PagedMenuHandler<M> l : allPages()) {
			action.accept(l.getMenu());
		}
	}

	public Stream<M> stream() {
		return allPages().stream().map(PagedMenuHandler::getMenu);
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
			Set<MenuView> set = new HashSet<>();
			set.add(view);
			cache.put(uuid, set);
		}
	}

	public static void clearCache(Player player) {
		cache.remove(player.getUniqueId());
		//P.p.log("Cache Cleared!");
	}

	public boolean isHead(M menu) {
		return pages.get(0).getMenu() == menu;
	}

	public PagedMenuHandler<M> getPage(int index) {
		if (index >= 0 && index < pages.size()) {
			return pages.get(index);
		}
		return null;
	}

	public PagedMenuHandler<M> getFirst() {
		return pages.get(0);
	}

	public boolean hasNext() {
		return (pages.size() > index + 1);
	}

	public PagedMenuHandler<M> getNext() {
		if (hasNext()) {
			return pages.get(index + 1);
		}
		return null;
	}

	public M getNextMenu() {
		if (hasNext()) {
			return pages.get(index + 1).getMenu();
		}
		return null;
	}

	public boolean hasPrev() {
		return (index - 1 >= 0);
	}

	public PagedMenuHandler<M> getPrev() {
		if (hasPrev()) {
			return pages.get(index - 1);
		}
		return null;
	}

	public M getPrevMenu() {
		if (hasPrev()) {
			return pages.get(index - 1).getMenu();
		}
		return null;
	}

	public PagedMenuHandler<M> getLast() {
		return pages.get(pages.size() - 1);
	}

	public List<PagedMenuHandler<M>> allPages() {
		return pages;
	}

	public int getNumPages() {
		return pages.size();
	}

	public int getPageIndex() {
		return index;
	}

	public void addPage(M... menus) {
		if (menus.length <= 0) return;
		int startIndex = getLast().getPageIndex();
		for (int i = 0; i < menus.length; i++) {
			M pmenu = menus[i];
			PagedMenuHandler<M> h = new PagedMenuHandler<>(pages, pmenu, i + startIndex + 1);
			pages.add(h);
			pmenu.setMenuPages(h);
			pmenu.showBasicButtons(true);
		}
		updateIndexes();
	}

	public void removePageIfEmpty() {
		if (pages.get(0) == this) {
			// We cannot remove the first page
			return;
		}
		int removeToIndex = getMenu().isShowingBasicButtons() ? 9 : 0;
		Button[] buttons = getMenu().getButtons();
		for (int i = buttons.length - 1; i >= removeToIndex; i--) {
			if (buttons[i] != null) {
				return;
			}
		}
		removePage();
	}

	public void removePage() {
		if (pages.size() <= 1) {
			// We cannot remove all pages
			return;
		}
		pages.remove(index);
		updateIndexes();
	}

	public void removePage(int index) {
		if (pages.size() <= 1) {
			// We cannot remove all pages
			return;
		}
		if (index >= 0 && pages.size() > index) {
			pages.remove(index);
			updateIndexes();
		}
	}

	public void removeAllOtherPages() {
		// Remove all but the first Page
		if (pages.size() > 1) {
			pages.subList(1, pages.size()).clear();
		}
		updateIndexes();
	}

	public boolean isPageOfThisMenu(Menu other) {
		return other instanceof PagedMenu && ((PagedMenu) other).getMenuPages().pages == pages;
	}

	private void updateIndexes() {
		int i = 0;
		for (PagedMenuHandler<M> page : pages) {
			page.index = i;

			if (i < pages.size() - 1) {
				page.getMenu().addButton(new BNextPage(page), BNextPage.getDefaultSlot());
			} else {
				page.getMenu().addButton(new BEmptyTopTile(), BNextPage.getDefaultSlot());
			}
			page.getMenu().updateSlot(BNextPage.getDefaultSlot());
			if (i > 0) {
				page.getMenu().addButton(new BPrevPage(page), BPrevPage.getDefaultSlot());
			} else {
				page.getMenu().addButton(new BEmptyTopTile(), BPrevPage.getDefaultSlot());
			}
			page.getMenu().updateSlot(BPrevPage.getDefaultSlot());
			i++;
		}
	}
}
