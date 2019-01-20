package com.snow.menu.Menus;

import com.snow.menu.Buttons.Button;
import com.snow.menu.Menus.Attributes.PagedMenuHandler;
import com.snow.menu.P;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

public class ListMenu<E> extends FullyPagedMenu {

	protected IntFunction<Button> intFunction;
	protected Collection<E> collection;
	protected Function<E, Button> function;
	protected BiFunction<E, Integer, Button> biFunction;

	private boolean autoUpdate = false;
	private int updateInterval = 600; //600 seconds = 10 min
	private long lastUpdate = 0;
	private boolean shouldRebuild = false;

	public ListMenu(String name, IntFunction<Button> function) {
		this(name, false);
		if (function == null) {
			throw new IllegalArgumentException("function cant be null");
		}
		this.intFunction = function;
		rebuildAll();
	}

	public ListMenu(String name, Collection<E> collection, Function<E, Button> function) {
		this(name, false);
		if (function == null) {
			throw new IllegalArgumentException("function cant be null");
		}
		if (collection == null) {
			throw new IllegalArgumentException("collection cant be null");
		}
		this.function = function;
		this.collection = collection;
		rebuildAll();
	}

	public ListMenu(String name, Collection<E> collection, BiFunction<E, Integer, Button> function) {
		this(name,  false);
		if (function == null) {
			throw new IllegalArgumentException("function cant be null");
		}
		if (collection == null) {
			throw new IllegalArgumentException("collection cant be null");
		}
		this.biFunction = function;
		this.collection = collection;
		rebuildAll();
	}

	public ListMenu(String name, boolean init) {
		super(name, init);
	}

	public static ListMenu<ItemStack> itemList(String name, Collection<ItemStack> itemStacks) {
		return new ListMenu<>(name, itemStacks, i -> new Button(i));
	}

	// This is called whenever a new Page has to be created by the Menu
	// Override this to return a new Menu Page of your class
	// If you want all the Pages in the Menu to be of your class
	@Override
	public ListMenu creatingNewMenuPage() {
		return new ListMenu(this.getName(), false);
	}

	protected Button applyFunction(E obj, int index) {
		if (intFunction != null) {
			return intFunction.apply(index);
		} else if (function != null) {
			return function.apply(obj);
		} else if (biFunction != null) {
			return biFunction.apply(obj, index);
		}
		return null;
	}

	public boolean addNonListButton(E obj) {
		if (function != null) {
			return super.addButton(function.apply(obj));
		}
		return false;
	}

	public boolean addNonListButton(E obj, int slot) {
		return super.addButton(applyFunction(obj, slot));
	}

	public Collection<E> getList() {
		return collection;
	}

	public void setList(Collection<E> collection) {
		this.collection = collection;
	}

	public boolean isAutoUpdate() {
		return autoUpdate;
	}

	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	public int getUpdateInterval() {
		return updateInterval;
	}

	public void setUpdateInterval(int updateInterval) {
		this.updateInterval = updateInterval;
	}

	public long getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(long lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public boolean isShouldRebuild() {
		return shouldRebuild;
	}

	public void setShouldRebuild(boolean shouldRebuild) {
		this.shouldRebuild = shouldRebuild;
	}

	@Override
	public boolean willOpenMenu(Player player) {
		if (getMenuPages() != null && !isHead()) return super.willOpenMenu(player);
		long time = System.currentTimeMillis();
		if (isShouldRebuild() || (isAutoUpdate() && time - lastUpdate > getUpdateInterval() * 1000)) {
			setLastUpdate(time);
			setShouldRebuild(false);
			rebuildAll();
		}
		return super.willOpenMenu(player);
	}

	public void rebuildAll() {
		if (intFunction != null) {
			init(intFunction);
		} else {
			init(collection, function, biFunction);
		}
		//P.p.log("Rebuilt a Menu");
	}

	private void init(IntFunction<Button> function) {
		int slot = 9;
		int index = 0;
		List<ListMenu> pages = new ArrayList<>();
		ListMenu currentPage = this;
		pages.add(this);

		Button b;
		while ((b = function.apply(index)) != null) {
			currentPage.addButtonThisPage(b, slot);
			slot++;
			if (slot > 53) {
				slot = 9;
				currentPage = creatingNewMenuPage();
				pages.add(currentPage);
			}
			index++;
		}
		PagedMenuHandler.init(pages.toArray(new ListMenu[0]));
	}

	private void init(Collection<E> collection, Function<E, Button> function, BiFunction<E, Integer, Button> bifunction) {
		if (collection == null || (function == null && bifunction == null)) {
			throw new IllegalArgumentException("collection and function cant be null");
		}
		int slot = 9;
		int index = 0;
		List<ListMenu> pages = new ArrayList<>();
		ListMenu currentPage = this;
		pages.add(this);

		for (E element : collection) {
			//ItemStack i = new ItemStack(mat);
			if (element != null) {
				if (function != null) {
					currentPage.addButtonThisPage(function.apply(element), slot);
				} else {
					currentPage.addButtonThisPage(bifunction.apply(element, index), slot);
				}
				slot++;
				if (slot > 53) {
					slot = 9;
					currentPage = creatingNewMenuPage();
					pages.add(currentPage);
				}
			}
			index++;
		}
		PagedMenuHandler.init(pages.toArray(new ListMenu[0]));
	}

	public void rebuildAllNextOpen() {
		setShouldRebuild(true);
	}

	public void refreshButton(int index) {
		Button b = null;
		if (intFunction != null) {
			b = intFunction.apply(index);
		} else if (collection instanceof List) {
			E e = ((List<E>) collection).get(index);
			if (e != null) {
				b = applyFunction(e, index);
			}
		} else {
			P.p.log("Tried to refresh button in non List");
			return;
		}
		super.removeButton(calcSlot(index));
		super.addButton(b);
		updateButton(b);
	}

	public void refreshButton(E obj) {
		if (collection instanceof List) {
			int index = ((List<E>) collection).indexOf(obj);
			int slot = calcSlot(index);
			super.removeButton(slot);
			if (obj != null) {
				super.addButton(applyFunction(obj, index));
				updateSlot(slot);
			}
		} else {
			P.p.log("Tried to refresh button in non List");
		}
	}

	// We dont count the Menu Top Bar, so for each new page we need to add 9 to the slot
	protected int calcSlot(int index) {
		int add = 1 + (int) ( ((float) index) / 54);
		return index + (add * 9);
	}
}
