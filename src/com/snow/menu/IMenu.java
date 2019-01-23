package com.snow.menu;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.snow.menu.Buttons.Button;
import com.snow.menu.Buttons.IButton;
import com.snow.menu.Util.ButtonIterator;

import java.util.List;

/**
 * Interface for the Base Menu Class.
 * Completely implemented by it and only used for Attributes
 */
public interface IMenu extends Iterable<Button> {
	// Adds Home, Back Buttons and Empty Panes to the Top of the Menu
	// Sort of like a Menu Bar on the Top
	IMenu showBasicButtons(boolean showHome);

	IMenu setNoBack(boolean noBack);

	boolean isNoBack();

	boolean isShowingBasicButtons();

	String getName();

	void setName(String name);

	int getRows();

	void setRows(int size);

	// Adds a Button to the next free Space in the Menu
	boolean addButton(Button button);

	// Add a Button to the Menu using row and column
	boolean addButton(Button button, int row, int column) throws IllegalArgumentException;

	// Add a Button to the Menu using row and column
	boolean addButton(Button button, int slot) throws IllegalArgumentException;

	Button getButton(int row, int column);

	Button getButton(int slot);

	Button[] getButtons();

	// Get All Buttons of a Certain Class
	// Can be used to loop over all Buttons of that Type
	// Returns an iterable that has an iterator() method or can simply be used in a foreach loop
	<T extends IButton> Iterable<T> getButtons(Class<T> clazz);

	// Remove a Button by row and column, returns Button that was removed
	Button removeButton(int row, int column);

	// Remove a Button by slot, returns Button that was removed
	Button removeButton(int slot);

	// Remove a Button from this menu, returns true if successful
	boolean removeButton(Button button);

	// Remove all buttons in this Menu
	// If alsoBasic is true, also remove the Top Bar where the Home and Back buttons might be
	void clear(boolean withTopRow);

	// Returns true if the Menu contains the given Button
	boolean contains(Button button);

	// Returns true if the Menu contains a Button that is or extends the given class
	boolean contains(Class<? extends Button> buttonType);

	// Opens this Menu to the specified player
	// Returns the view the Player is having to the menu on success
	// Returns null if the player is dead, etc. or Menu size is < 0
	MenuView show(Player player);

	// The Player clicked on a slot in the menu inventory that contains an item
	void clickOnSlot(InventoryClickEvent event, MenuView view);

	// The Player clicked outside the menu inventory, or in the player inv
	void onClickOnPlayerInv(InventoryClickEvent event, MenuView view);

	// The Player clicked an emply slot inside the menu inventory
	void onClickOnEmptySlot(InventoryClickEvent event, MenuView view);

	// The Menu will open for the Player, The old Menu (if any) is still open
	// Do any changes/updates to Buttons here
	// Return false if the Menu should not be opened
	boolean onPrepareOpeningMenu(Player player);

	// The Menu is opened for a player, or opened again
	// fresh is true if the menu was just created and not just opened again
	void onOpeningMenu(Player player, MenuView view, boolean fresh, MenuView old);

	// The Player closes the Menu, it is being force closed, or another Menu opens
	void onClosingMenu(Player player, MenuView view, MenuView target);

	// If the Player can admin the Menu,
	// Determines if the player can do certain actions like editing an EditableMenu.
	// Also to be used by buttons inside the Menu, like determining if a buttons text can be edited by the Player
	// Returns true for OP players by default
	boolean canAdmin(Player player);

	// Get the last time one of the update methods was called
	// equivalent to the last time a change was made to the menu which was updated to viewing players
	// used by cached views to check if they need to update
	// time in millis
	long getLastChangeTime();

	// Set the Last Change time to the current time
	void resetLastChangeTime();

	// Updates the Menu to players that are having the menu open currently at the moment
	// Needs to be called after big changes were made that should be immediately visible
	void update();

	// Updates a specific Button of the Menu to players viewing the Menu
	// Needs to be called after the Button was changed
	void updateButton(Button button);

	// Updates the specified slot of the Menu to players viewing the Menu
	// Needs to be called after one Button was changed
	void updateSlot(int slot);

	<T extends IButton> ButtonIterator<T> iterator(Class<T> clazz);

	List<MenuView> getOpenViews();

	// Returns true if any Player is currently viewing this Menu
	public boolean hasOpenViews();

	// keeps track of opened and closed views
	void listOpenView(MenuView view);

	void listCloseView(MenuView view);
}
