package com.snow.menu.Menus;

import com.snow.menu.Buttons.Button;
import com.snow.menu.Buttons.Tools.ButtonCreator;
import com.snow.menu.Menus.Attributes.PagedMenuHandler;
import com.snow.menu.P;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;

/*
  A Paged Menu that is automatically populated from a Collection of any Object E
  It uses the given Function to get a Button for each Object
  It will display a Button for each Object in the Collection
  The Menu can be set to auto update from the Collection after a certain time
  Shows Top Bar with Basic Buttons by default
 */

public class ListMenu<E> extends FullyPagedMenu {

	protected IntFunction<Button> intFunction;
	protected Collection<E> collection;
	protected Function<E, Button> function;
	protected BiFunction<E, Integer, Button> biFunction;

	private boolean autoUpdate = false;
	private int updateInterval = 600; //600 seconds = 10 min
	private long lastUpdate = 0;
	private boolean shouldRebuild = false;

	/*
	  ListMenu that uses no Collection but rather is populated by
	  a function that returns a Button given the index
	  Will continue adding Buttons until the function returns null
	 */
	public ListMenu(String name, IntFunction<Button> function) {
		this(name, false);
		if (function == null) {
			throw new IllegalArgumentException("function cant be null");
		}
		this.intFunction = function;
		rebuildAll();
	}

	/*
	  ListMenu that is populated using the given collection and function
	  For each Element E of the collection, the function is called and should return
	  the corresponding Button
	 */
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

	/*
	  The same as above but the function gets the Element E and its index as arguments
	 */
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

	/*
	  List Menu without a populator so it will be Empty
	  init determines if the PageHandler should be initialized
	  Without overriding anything in this Class, this will be functionally the same as FullyPagedMenu
	 */
	public ListMenu(String name, boolean init) {
		super(name, init);
	}

	/*
	  Creates a ListMenu from a Collection of Buttons
	 */
	public static ListMenu<Button> withButtons(String name, Collection<Button> buttons) {
		return new ListMenu<>(name, buttons, b -> b);
	}

	/*
	  Creates a ListMenu from a Collection of ButtonCreators
	  A ButtonCreator is an object that can be directly created into a Button
	 */
	public static ListMenu<ButtonCreator> withButtonCreators(String name, Collection<ButtonCreator> buttonCreators) {
		return new ListMenu<>(name, buttonCreators, ButtonCreator::createButton);
	}

	/*
	  Creates a ListMenu with a Button for each ItemStack in the given Collection
	 */
	public static ListMenu<ItemStack> withItemStacks(String name, Collection<ItemStack> itemStacks) {
		return new ListMenu<>(name, itemStacks, i -> new Button(i));
	}

	// This is called whenever a new Page has to be created by the Menu
	// You can Override this to return a new Menu Page of your class
	// If you want all the Pages in the Menu to be of your class
	// This should only be necessary if you override Methods that need to apply to every Page
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

	// Add a Button to the Menu that is not included in the Collection
	// This is the same as addButton() but it takes an element E and applies the function
	public boolean addNonListButton(E obj) {
		if (function != null) {
			Button button = function.apply(obj);
			if (super.addButton(button)) {
				updateButton(button);
				return true;
			}
		}
		return false;
	}

	// Add a Button to the Menu that is not included in the Collection
	// This is the same as addButton() but it takes an element E and applies the function
	// The index has to be given if the function needs an index as argument
	public boolean addNonListButton(E obj, int index) {
		Button button = applyFunction(obj, index);
		if (super.addButton(button)) {
			updateButton(button);
			return true;
		}
		return false;
	}

	// Get The underlying List that this Menu is populated from
	public Collection<E> getList() {
		return collection;
	}

	// Set The underlying List that this Menu is populated from
	public void setList(Collection<E> collection) {
		this.collection = collection;
	}

	public boolean isAutoUpdate() {
		return autoUpdate;
	}

	public void setAutoUpdate(boolean autoUpdate) {
		this.autoUpdate = autoUpdate;
	}

	// Update Interval in seconds
	public int getUpdateInterval() {
		return updateInterval;
	}

	// Update Interval in seconds
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
	public boolean onPrepareOpeningMenu(Player player) {
		if (getMenuPages() != null && !isHead()) return super.onPrepareOpeningMenu(player);
		long time = System.currentTimeMillis();
		if (isShouldRebuild() || (isAutoUpdate() && time - lastUpdate > getUpdateInterval() * 1000)) {
			rebuildAll();
		}
		return super.onPrepareOpeningMenu(player);
	}

	public void rebuildAll() {
		setLastUpdate(System.currentTimeMillis());
		setShouldRebuild(false);
		// Clear only this Page, the others will fall away and be regenerated
		clearThisPage(false);
		if (intFunction != null) {
			init(intFunction);
		} else {
			init(collection, function, biFunction);
		}
		P.p.log("Rebuilt Menu: " + getName());
	}

	public void rebuildAllNextOpen() {
		setShouldRebuild(true);
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

	// Refresh a Button from the List at the specified index
	// The Collection this menu is updating from must be a List
	// This also works on ListMenus without a Collection using the index Function
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
			throw new IllegalStateException("Tried to refresh button in non-List: " + collection.getClass().getSimpleName());
		}
		super.removeButton(calcSlot(index));
		super.addButton(b);
		updateButton(b);
	}

	// Refresh a certain Button from the List
	// The Collection this menu is updating from must be a List
	public void refreshButton(E obj) {
		if (collection instanceof List) {
			int index = ((List<E>) collection).indexOf(obj);
			if (index >= 0) {
				int slot = calcSlot(index);
				super.removeButton(slot);
				if (obj != null) {
					super.addButton(applyFunction(obj, index));
					updateSlot(slot);
				}
			} else {
				throw new NoSuchElementException("Tried to refresh Button thats not in the List of this ListMenu");
			}
		} else {
			throw new IllegalStateException("Tried to refresh button in non-List: " + collection.getClass().getSimpleName());
		}
	}

	// We dont count the Menu Top Bar, so for each new page we need to add 9 to the index
	public static int calcSlot(int index) {
		// On each page we have, due to the Menu Bar, 45 usable slots.
		// So for every 45 in the index we need a new page
		// We have 1 page at the start + another for each 45 items
		int pages = 1 + (int) ( ((float) index) / 45f );
		// So add 9 slots (menubar) to the index for every page we have
		return index + (pages * 9);
	}
}
