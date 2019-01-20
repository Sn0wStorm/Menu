package com.snow.menu.Buttons;

import java.util.List;
import java.util.function.BiConsumer;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Menu;
import com.snow.menu.MenuView;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Interface for the Base Button Class.
 * Completely implemented by it and only used for Attributes
 */
public interface IButton {
	// Give the Button Function in form of a Lambda Expression or Function Reference
	// will be executed when the Button is clicked
	// example:   setFunction((player, view) -> player.sendMessage("clicked a button"));
	IButton setClickFunction(BiConsumer<Player, MenuView> function);

	// Setting the Button to be disabled makes not show in menus
	IButton setDisabled(boolean disabled);

	// Set whether the Button should be able to be moved in an EditableMenu
	// Default is true
	// Setting to false is the same as implementing ImmovableButton
	IButton setMovable(boolean movable);

	// Can set the Click delay for every Player using this Button in milliseconds
	// See Method getClickDelay() below
	// sameButton is the Delay between clicks on this Button only
	// differentButton is the Delay if the Player clicked an other Button before
	IButton setClickDelay(int sameButton, int differentButton);

	boolean isMovable();

	boolean isDisabled();

	// Button was clicked by a Player
	// The Event is already cancelled and can be checked for clicktype etc.
	void click(InventoryClickEvent event, MenuView view);

	// Button was clicked by a Player, but he is not allowed to (canclick returned false)
	// The Event is already cancelled and can be checked for clicktype etc.
	void clickNotAllowed(InventoryClickEvent event, MenuView view);

	// Should return false if the Button should not be shown for the Player
	// Permission checks, etc can be done here
	boolean canSee(Player player, MenuView view);

	// Should return false if the Button can not be clicked by the Player
	// Will call the method clickNotAllowed instead of click when clicked
	boolean canClick(Player player, MenuView view);

	// Returns the Delay that should be between clicks in miliseconds
	// If sameButton is true, the click was on the same button as before
	// So if multiple fast clicks on the same button should be avoided, do that if sameButton is true
	// Override this for buttons that should not be clicked often (maybe due to processing power)
	int getClickDelay(Player player, MenuView view, boolean sameButton);

	// Method that is called by the Menu to get the item to display
	// Returns the ItemStack that should display for this player
	// A StatedButton for example should override this to display a certain state for this player
	ItemStack getItem(Player player, MenuView view);

	// Called when the Button is removed from a Menu
	void removing();

	// Called when the Button is being added to a Menu
	void adding(Menu menu, int slot);

	// Update this Button in a view for a player
	void update(MenuView view, Player player);

	// Update this Button for all Players viewing the Menu it is in
	void update();

	Menu getCurrentMenu();

	void setCurrentMenu(Menu menu, int slot);

	// Get the Buttonhandler specified by the given class
	// Null if not applied to item
	/*<T extends ButtonHandler> T getHandler(Class<T> clazz);

	ButtonHandler[] getHandlers();*/

	String getName();

	void setName(String name);

	Material getType();

	void setType(Material type);

	List<String> getLore();

	void setLore(List<String> lore);

	// returns null if line doesnt exist
	String getLoreLine(int line);

	void setLoreLine(int line, String loreLine);

	void addLore(String... lines);

	void setGlowing(boolean glow);

	int getAmount();

	void setAmount(int amount);

	ItemMeta getItemMeta();

	void setItemMeta(ItemMeta meta);

	// Returns the actual item representing the Button
	ItemStack getItem();

	// Set the representing item
	// will not be set on currently opened menus, unless currentMenu.update() is called
	void setItem(ItemStack item);
}
