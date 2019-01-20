package com.snow.menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

import com.snow.menu.Menus.Attributes.NoBackMenu;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import com.snow.menu.Buttons.Basic.BBack;
import com.snow.menu.Buttons.Basic.BEmptyTopTile;
import com.snow.menu.Buttons.Basic.BHome;
import com.snow.menu.Buttons.Button;
import com.snow.menu.Buttons.IButton;
import com.snow.menu.Util.ButtonIterator;

 /*
    A Menu where buttons can be added
    only needs be instantiated once and can be shown to multiple players
    can be extended to make use of the alternate click methods
  */

public class Menu implements IMenu {

	private int size; // Number of rows
	private String name; // Name of the shown inventory
	private Button[] buttons; // Array of all included buttons
	private List<MenuView> currentViews = new ArrayList<MenuView>(); // MenuViews (players) that are currently viewing the menu
	private boolean showBasic; // If basic controls like home and back should be shown
	private boolean noBack; // If this Menu cannot be reached via a Back Button

	public Menu(String name) {
		this(name, 6);
	}

	public Menu(String name, int size) {
		this.name = name;
		this.size = size;
		if (size <= 0) {
			buttons = new Button[0];
		} else {
			buttons = new Button[size * 9];
		}
	}


	/*   -------  Settings to change on the Menu, makes change for all Players  -------   */
	/*   -------  These return their Menu for chaining  -------   */

	// Adds Home, Back Buttons and Empty Panes to the Top of the Menu
	// Sort of like a Menu Bar on the Top
	// ShowHome determines if the Home Button to the main Menu should be shown
	@Override
	public Menu showBasicButtons(boolean showHome) {
		if (showBasic) return this;
		if (showHome) {
			addButton(new BHome(), BHome.getDefaultSlot());
		}
		addButton(new BBack(), BBack.getDefaultSlot());
		for (int i = 0; i < 9; i++) {
			if (buttons[i] == null) {
				addButton(new BEmptyTopTile(), i);
			}
		}
		showBasic = true;
		return this;
	}

	/**
	 * When true, this menu cannot be reached via a Back Button (default false)
	 * Clicking the Back button will go to the Menu opened prior to this
	 * Useful for Menus that should only show once or at certain times, like Item Selector Menus etc.
	 * Setting this to true is the same as setting implementing NoBackMenu
	 */
	@Override
	public Menu setNoBack(boolean noBack) {
		this.noBack = noBack;
		return this;
	}

	@Override
	public boolean isNoBack() {
		return noBack || this instanceof NoBackMenu;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getRows() {
		return size;
	}

	@Override
	public void setRows(int size) {
		this.size = size;
		buttons = Arrays.copyOf(buttons, size * 9);
	}

	// Adds a Button to the next free Space in the Menu
	// If the Basic Buttons (Menu Bar) is shown, add below
	@Override
	public boolean addButton(Button button) throws IllegalArgumentException {
		if (button == null) {
			throw new IllegalArgumentException("Button cannot be null, use removeButton to remove a Button");
		}
		int slot = 0;
		if (showBasic) {
			slot = 9;
		}
		while (slot < buttons.length) {
			if (buttons[slot] == null) {
				buttons[slot] = button;
				button.adding(this, slot);
				return true;
			}
			slot++;
		}
		return false;
	}

	// Add a Button to the Menu using row and column
	@Override
	public boolean addButton(Button button, int row, int column) throws IllegalArgumentException {
		if (button == null) {
			throw new IllegalArgumentException("Button cannot be null, use removeButton to remove a Button");
		}
		if (column > 8) {
			throw new IllegalArgumentException("Invalid Value for Column: " + column + ", valid values are 0-8");
		}
		if (row < 0) {
			throw new IllegalArgumentException("Invalid Value for Row: " + column + ", must be higher than 0");
		}

		if (row + 1 > size) {
			return false;
		}

		return addButton(button, (row * 9) + column);
	}

	// Add a Button to the Menu using row and column
	@Override
	public boolean addButton(Button button, int slot) throws IllegalArgumentException {
		if (button == null) {
			throw new IllegalArgumentException("Button cannot be null, use removeButton to remove a Button");
		}
		if (slot < 0) {
			throw new IllegalArgumentException("Invalid Value for Slot: " + slot + ", must be higher than 0");
		}

		if (slot + 1 > size * 9) {
			return false;
		}

		if (buttons[slot] != null) {
			removeButton(slot);
		}

		buttons[slot] = button;
		button.adding(this, slot);
		//updateSlot(slot);

		return true;
	}

	@Override
	public Button getButton(int row, int column) {
		return getButton((row * 9) + column);
	}

	@Override
	public Button getButton(int slot) {
		if (slot >= buttons.length) {
			return null;
		}
		return buttons[slot];
	}

	@Override
	public Button[] getButtons() {
		return buttons;
	}

	// Get All Buttons of a Certain Class
	// Can be used to loop over all Buttons of that Type
	// Returns an iterable that has an iterator() method or can simply be used in a foreach loop
	@Override
	public <T extends IButton> Iterable<T> getButtons(final Class<T> clazz) {
		return new Iterable<T>() {
			@Override
			public Iterator<T> iterator() {
				return Menu.this.iterator(clazz);
			}
		};
	}

	public Stream<Button> stream() {
		return Arrays.stream(buttons);
	}

	// Remove a Button by row and column, returns Button that was removed
	@Override
	public Button removeButton(int row, int column) {
		return removeButton((row * 9) + column);
	}

	// Remove a Button by slot, returns Button that was removed
	@Override
	public Button removeButton(int slot) {
		Button button = buttons[slot];
		if (button != null) {
			button.removing();
			buttons[slot] = null;
			//updateSlot(slot);
		}
		return button;
	}

	// Remove a Button from this menu, returns true if successful
	@Override
	public boolean removeButton(Button button) {
		if (button == null) {
			return false;
		}
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i] == null) {
				continue;
			}
			if (buttons[i].equals(button)) {
				button.removing();
				buttons[i] = null;
				return true;
			}
		}
		return false;
	}

	// Returns true if the Menu contains the given Button
	@Override
	public boolean contains(Button button) {
		if (button == null) return false;
		for (Button one : buttons) {
			if (one != null && one.equals(button)) {
				return true;
			}
		}
		return false;
	}

	// Returns true if the Menu contains a Button that is or extends the given class
	@Override
	public boolean contains(Class<? extends Button> buttonType) {
		for (Button one : buttons) {
			if (one != null && buttonType.isAssignableFrom(one.getClass())) {
				return true;
			}
		}
		return false;
	}

	// Opens this Menu to the specified player
	// Returns the view the Player is having to the menu on success
	// Returns null if the player is dead, etc. or Menu size is < 0
	@Override
	public MenuView show(Player player) {
		if (size <= 0) {
			return null;
		}
		if (!player.isOnline() || player.isDead() || !player.isValid()) {
			return null;
		}

		if (!willOpenMenu(player)) {
			return null;
		}

		MenuView view = new MenuView(this);
		Inventory inv;
		if (name != null) {
			inv = P.p.getServer().createInventory(view, size * 9, name);
		} else {
			inv = P.p.getServer().createInventory(view, size * 9);
		}
		view.setInventory(inv);

		MenuView old = MenuView.getOpenView(player);
		if (old != null) {
			view.setBackMenu(old);
			MenuListener.storeClosing(player.getUniqueId(), old);
			old.getMenu().closingMenu(player, old, view);
			old.closed();
		}

		updateButtons(inv, view, player, true);

		openingMenu(player, view, true, old);

		listOpenView(view);
		player.openInventory(inv);
		return view;
	}

	// The Player clicked on a slot in the menu inventory that contains an item
	@Override
	public void clickOnSlot(InventoryClickEvent event, MenuView view) {
		int slot = event.getSlot();
		if (buttons[slot] == null) {
			P.p.log("There should be no button!");
			return;
		}

		Player player = ((Player) event.getWhoClicked());
		if (!ClickSpam.canClick(buttons[slot], player, view)) {
			player.sendMessage("§cNicht so schnell klicken!");
			return;
		}
		if (buttons[slot].canClick(player, view)) {
			buttons[slot].click(event, view);
		} else {
			buttons[slot].clickNotAllowed(event, view);
		}
	}

	// The Player clicked outside the menu inventory, or in the player inv
	@Override
	public void clickOnPlayerInv(InventoryClickEvent event, MenuView view) {
		if (showBasic) {
			if (event.getSlotType().equals(InventoryType.SlotType.OUTSIDE)) {
				if (view.getBackMenu() != null) {
					view.getBackMenu().showAgain(((Player) event.getWhoClicked()), view);
				}
			}
		}
	}

	// The Player clicked an empty slot inside the menu inventory
	@Override
	public void clickOnEmptySlot(InventoryClickEvent event, MenuView view) {
	}

	// The Menu will open for the Player, The old Menu (if any) is still open
	// Do any changes/updates to Buttons here
	// Return false if the Menu should not be opened
	@Override
	public boolean willOpenMenu(Player player) {
		return true;
	}

	// The Menu will be opened for a player, or opened again
	// Everything including Buttons is already done and set and the Menu will open right after
	// fresh is true if the menu was just created and not just opened again
	// The MenuView the player is coming from is given with old
	// The old MenuView may be null if the player was not in a menu before
	@Override
	public void openingMenu(Player player, MenuView view, boolean fresh, MenuView old) {
	}

	// The Player closes the Menu, it is being force closed, or another Menu opens
	// If the player opens another menu, the target is that new menu
	@Override
	public void closingMenu(Player player, MenuView view, MenuView target) {
	}

	// If the Player can admin the Menu,
	// Determines if the player can do certain actions like editing an EditableMenu.
	// Also to be used by buttons inside the Menu, like determining if a buttons text can be edited by the Player
	// Returns true for OP players by default
	@Override
	public boolean canAdmin(Player player) {
		return player.isOp();
	}

	// Updates the Menu to players that are having the menu open currently
	// Needs to be called after big changes were made that should be immediately visible
	@Override
	public void update() {
		if (!hasOpenViews()) return;

		MenuView view;
		for (ListIterator<MenuView> iter = currentViews.listIterator(); iter.hasNext();) {
			view = iter.next();
			Inventory inv = view.getInventory();
			if (inv == null) {
				iter.remove();
				continue;
			}
			List<HumanEntity> viewers = inv.getViewers();
			if (viewers.isEmpty()) {
				iter.remove();
				continue;
			} else if (viewers.size() > 1) {
				P.p.log("Too many viewers!");
			}

			if (viewers.get(0) instanceof Player) {
				Player player = (Player) viewers.get(0);
				updateButtons(inv, view, player, false);
				player.updateInventory();
			}
		}
	}

	// Updates a specific Button of the Menu to players viewing the Menu
	// Needs to be called after the Button was changed
	@Override
	public void updateButton(Button button) {
		if (!hasOpenViews()) return;

		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i] == button) {
				updateSlot(i);
				return;
			}
		}
	}

	// Updates the specified slot of the Menu to players viewing the Menu
	// Needs to be called after one Button was changed
	@Override
	public void updateSlot(int slot) {
		if (!hasOpenViews()) return;

		MenuView view;
		for (ListIterator<MenuView> iter = currentViews.listIterator(); iter.hasNext();) {
			view = iter.next();
			Inventory inv = view.getInventory();
			if (inv == null) {
				iter.remove();
				continue;
			}
			List<HumanEntity> viewers = inv.getViewers();
			if (viewers.isEmpty()) {
				iter.remove();
				continue;
			} else if (viewers.size() > 1) {
				P.p.log("Too many viewers!");
			}

			if (viewers.get(0) instanceof Player) {
				Player player = (Player) viewers.get(0);
				if (buttons[slot] == null) {
					inv.setItem(slot, null);
				} else {
					if (buttons[slot].canSee(player, view)) {
						inv.setItem(slot, buttons[slot].getItem(player, view));
					} else {
						inv.setItem(slot, null);
					}
				}
				player.updateInventory();
			}
		}
	}

	private void updateButtons(Inventory inv, MenuView view, Player player, boolean fresh) {
		for (int i = 0; i < inv.getSize() && i < buttons.length; i++) {
			if (buttons[i] == null) {
				if (!fresh) {
					inv.setItem(i, null);
				}
				continue;
			}
			if (buttons[i].canSee(player, view)) {
				inv.setItem(i, buttons[i].getItem(player, view));
			} else if (!fresh) {
				inv.setItem(i, null);
			}
		}
	}

	// Iterate over all Buttons in this Menu
	// Does not return null Buttons
	@Override
	public ButtonIterator<Button> iterator() {
		return new ButtonIterator<Button>(this);
	}

	// Iterate over all Buttons in this Menu that are assignable to the given class
	@Override
	public <T extends IButton> ButtonIterator<T> iterator(Class<T> clazz) {
		return new ButtonIterator<T>(this, clazz);
	}

	// Returns true if any Player is currently viewing this Menu
	@Override
	public boolean hasOpenViews() {
		return !currentViews.isEmpty();
	}

	// keeps track of opened and closed views
	@Override
	public void listOpenView(MenuView view) {
		currentViews.add(view);
	}

	@Override
	public void listCloseView(MenuView view) {
		currentViews.remove(view);
	}
}
