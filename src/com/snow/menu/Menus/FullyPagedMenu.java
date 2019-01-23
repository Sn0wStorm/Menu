package com.snow.menu.Menus;

import com.snow.menu.Buttons.Button;
import com.snow.menu.Menu;
import com.snow.menu.MenuView;
import com.snow.menu.Menus.Attributes.PagedMenu;
import com.snow.menu.Menus.Attributes.PagedMenuHandler;
import org.bukkit.entity.Player;

import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.function.Consumer;
import java.util.stream.Stream;


/*
  A Menu with Pages that is fully integrated as Menu.
  Adding and removing Buttons will create and remove Pages if needed
  Most operations on the First Page are modified to work across on all pages
  Shows Top Bar with Basic Buttons by default
 */

public class FullyPagedMenu extends Menu implements PagedMenu {

	protected PagedMenuHandler<FullyPagedMenu> pages;
	//protected PagedMenuHandler<FullyPagedMenu> lastChangedPage = null;

	public FullyPagedMenu(String name) {
		super(name);
	}

	public FullyPagedMenu(String name, boolean init) {
		super(name);
		if (init) PagedMenuHandler.init(this);
	}

	public FullyPagedMenu(String name, int size, boolean init) {
		super(name, size);
		if (init) PagedMenuHandler.init(this);
	}


	// This is called whenever a new Page has to be created by the Menu
	// Override this to return a new Menu Page of your class
	// If you want all the Pages to be of your class
	public FullyPagedMenu creatingNewMenuPage() {
		return new FullyPagedMenu(getName(), false);
	}


	public void forAllPages(Consumer<FullyPagedMenu> action) {
		getMenuPages().forAllPages(action);
	}

	@Override
	public PagedMenuHandler<FullyPagedMenu> getMenuPages() {
		/*List<String> list = new ArrayList<>();
		list.add("Hallo");
		list.add("Peter");
		new ListMenu("TEsT", list, (s -> new Button(Material.STONE, s)));*/
		return pages;
	}

	public Stream<FullyPagedMenu> streamPages() {
		return getMenuPages().stream();
	}

	@Override
	public void setMenuPages(PagedMenuHandler p) {
		pages = (PagedMenuHandler<FullyPagedMenu>) p;
	}

	public boolean isHead() {
		return pages.isHead(this);
	}

	@Override
	public void setName(String name) {
		if (isHead()) {
			forAllPages((p) -> p.setNameThisPage(name));
		} else {
			setNameThisPage(name);
		}
	}

	public void setNameThisPage(String name) {
		super.setName(name);
	}

	@Override
	public boolean addButton(Button button) throws IllegalArgumentException {
		if (!isHead()) {
			return addButtonThisPage(button);
		}
		for (PagedMenuHandler<FullyPagedMenu> p : getMenuPages().allPages()) {
			if (p.getMenu().addButtonThisPage(button)) {
				//lastChangedPage = p;
				return true;
			}
		}
		FullyPagedMenu m = creatingNewMenuPage();
		getMenuPages().addPage(m);
		m.addButtonThisPage(button);
		return true;
	}

	public boolean addButtonThisPage(Button button) throws IllegalArgumentException {
		return super.addButton(button);
	}

	@Override
	public boolean addButton(Button button, int slot) throws IllegalArgumentException {
		if (!isHead()) {
			return addButtonThisPage(button, slot);
		}
		for (PagedMenuHandler<FullyPagedMenu> p : getMenuPages().allPages()) {
			if (slot >= p.getMenu().getRows() * 9) {
				slot -= p.getMenu().getRows() * 9;
			} else {
				return p.getMenu().addButtonThisPage(button, slot);
			}
		}
		FullyPagedMenu f = creatingNewMenuPage();
		if (slot > f.getRows() * 9) {
			throw new IndexOutOfBoundsException("slot is not applicable to any menu page");
		}
		getMenuPages().addPage(f);
		return f.addButtonThisPage(button, slot);
	}

	public boolean addButtonThisPage(Button button, int slot) throws IllegalArgumentException {
		return super.addButton(button, slot);
	}

	@Override
	public Button getButton(int slot) {
		if (!isHead()) {
			return getButtonThisPage(slot);
		}
		for (PagedMenuHandler<FullyPagedMenu> p : getMenuPages().allPages()) {
			if (slot >= p.getMenu().getRows() * 9) {
				slot -= p.getMenu().getRows() * 9;
			} else {
				return p.getMenu().getButtonThisPage(slot);
			}
		}
		return null;
	}

	public Button getButtonThisPage(int slot) {
		return super.getButton(slot);
	}

	@Override
	public Button removeButton(int slot) {
		if (!isHead()) {
			return removeButtonThisPage(slot);
		}
		for (PagedMenuHandler<FullyPagedMenu> p : getMenuPages().allPages()) {
			if (slot >= p.getMenu().getRows() * 9) {
				slot -= p.getMenu().getRows() * 9;
			} else {
				Button b = p.getMenu().removeButtonThisPage(slot);
				if (b != null) {
					p.removePageIfEmpty();
				}
				return b;
			}
		}
		return null;
	}

	public Button removeButtonThisPage(int slot) {
		return super.removeButton(slot);
	}

	@Override
	public boolean removeButton(Button button) {
		if (!isHead()) {
			return removeButtonThisPage(button);
		}
		for (PagedMenuHandler<FullyPagedMenu> p : getMenuPages().allPages()) {
			if (p.getMenu().removeButtonThisPage(button)) {
				p.removePageIfEmpty();
				return true;
			}
		}
		return false;
	}

	public boolean removeButtonThisPage(Button button) {
		return super.removeButton(button);
	}

	@Override
	public boolean contains(Button button) {
		if (!isHead()) {
			return containsThisPage(button);
		}
		for (PagedMenuHandler<FullyPagedMenu> p : getMenuPages().allPages()) {
			if (p.getMenu().containsThisPage(button)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsThisPage(Button button) {
		return super.contains(button);
	}

	@Override
	public boolean contains(Class<? extends Button> buttonType) {
		if (!isHead()) {
			return containsThisPage(buttonType);
		}
		for (PagedMenuHandler<FullyPagedMenu> p : getMenuPages().allPages()) {
			if (p.getMenu().containsThisPage(buttonType)) {
				return true;
			}
		}
		return false;
	}

	public boolean containsThisPage(Class<? extends Button> buttonType) {
		return super.contains(buttonType);
	}

	@Override
	public void updateButton(Button button) {
		if (!isHead()) {
			updateButtonThisPage(button);
			return;
		}
		for (PagedMenuHandler<FullyPagedMenu> p : getMenuPages().allPages()) {
			if (p.getMenu().containsThisPage(button)) {
				p.getMenu().updateButtonThisPage(button);
				return;
			}
		}
	}

	public void updateButtonThisPage(Button button) {
		super.updateButton(button);
	}

	@Override
	public void updateSlot(int slot) {
		if (!isHead()) {
			updateSlotThisPage(slot);
			return;
		}
		for (PagedMenuHandler<FullyPagedMenu> p : getMenuPages().allPages()) {
			if (slot >= p.getMenu().getRows() * 9) {
				slot -= p.getMenu().getRows() * 9;
			} else {
				p.getMenu().updateSlotThisPage(slot);
				return;
			}
		}
	}

	public void updateSlotThisPage(int slot) {
		super.updateSlot(slot);
	}

	// When Overriding this, call super.onPrepareOpeningMenu(player)
	// to update the Next/Prev Page Buttons
	@Override
	public boolean onPrepareOpeningMenu(Player player) {
		if (super.onPrepareOpeningMenu(player)) {
			if (getMenuPages() == null) {
				throw new IllegalStateException("Paged Menu is not initialized");
			}

			/*Button b = getButton(BNextPage.getDefaultRow(), BNextPage.getDefaultColumn());
			if (b instanceof BNextPage) {
				b.setLoreLine(0, "Seite " + (getMenuPages().getPageIndex() + 1) + "/" + getMenuPages().getNumPages());
			}
			b = getButton(BPrevPage.getDefaultRow(), BPrevPage.getDefaultColumn());
			if (b instanceof BPrevPage) {
				b.setLoreLine(0, "Seite " + (getMenuPages().getPageIndex() + 1) + "/" + getMenuPages().getNumPages());
			}*/
		}
		return true;
	}

	@Override
	@OverridingMethodsMustInvokeSuper // Call super.onOpeningMenu when overriding this
	public void onOpeningMenu(Player player, MenuView view, boolean fresh, MenuView old) {
		super.onOpeningMenu(player, view, fresh, old);
		if (fresh) {
			PagedMenuHandler.cache(player.getUniqueId(), view);
		}
	}

	@Override
	@OverridingMethodsMustInvokeSuper // Call super.onClosingMenu when overriding this
	public void onClosingMenu(Player player, MenuView view, MenuView target) {
		PagedMenu.close(player, view, target);
	}
}
