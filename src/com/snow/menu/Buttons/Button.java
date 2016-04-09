package com.snow.menu.Buttons;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.snow.menu.Menu;
import com.snow.menu.MenuView;
import com.snow.menu.Util.NMSUtil;
import com.snow.menu.P;

  /*
    A Button that can be added to a Menu
    should be extended to add functionality other than showing text
    each Button instance can only be added to one menu
  */

public class Button implements IButton {

	// Menu this button is in
	// currentMenu.update() to update all Buttons immediately to menus currently open
	public Menu currentMenu;

	// item that represents this button
	private ItemStack item;

	// Settings
	private boolean movable = true;
	private int click1 = 400, click2 = 150;

	/*public Button() {
		item = new ItemStack(Material.STONE);
		item.setItemMeta(P.p.getServer().getItemFactory().getItemMeta(Material.STONE));
	}*/


	/*   -------  Constructors  -------   */

	public Button(Material type) {
		item = new ItemStack(type);
		item.setItemMeta(P.p.getServer().getItemFactory().getItemMeta(type));
	}

	public Button(Material type, short durability) {
		item = new ItemStack(type);
		item.setItemMeta(P.p.getServer().getItemFactory().getItemMeta(type));
		item.setDurability(durability);
	}

	public Button(Material type, String name) {
		this(type, (short) -1, name);
	}

	public Button(Material type, short durability, String name) {
		this(type, durability, name, (String[]) null);
	}

	public Button(Material type, String name, String... lore) {
		this(type, (short) -1, name, lore);
	}

	public Button(Material type, short durability, String name, String... lore) {
		item = new ItemStack(type);
		if (durability != -1) {
			item.setDurability(durability);
		}
		ItemMeta meta = P.p.getServer().getItemFactory().getItemMeta(type);

		if (name != null) {
			meta.setDisplayName(nameColor(name));
		}
		if (lore != null) {
			List<String> l = new ArrayList<String>();
			for (String line : lore) {
				l.add(loreColor(line));
			}
			meta.setLore(l);
		}
		item.setItemMeta(meta);
	}

	public Button(ItemStack item) {
		this.item = item;
		if (!item.hasItemMeta()) {
			item.setItemMeta(P.p.getServer().getItemFactory().getItemMeta(item.getType()));
		}
	}


	/*   -------  Settings to change on the Button, makes change for all Players  -------   */
	/*   -------  These return the Button instance for chaining  -------   */

	// Set whether the Button should be able to be moved in an EditableMenu
	// Default is true
	// Setting to false is the same as implementing ImmovableButton
	@Override
	public Button setMovable(boolean movable) {
		this.movable = movable;
		return this;
	}

	// Can set the Click delay for every Player using this Button in milliseconds
	// See Method getClickDelay() below
	// sameButton is the Delay between clicks on this Button only
	// differentButton is the Delay if the Player clicked an other Button before
	@Override
	public Button setClickDelay(int sameButton, int differentButton) {
		click1 = sameButton;
		click2 = differentButton;
		return this;
	}

	@Override
	public boolean isMovable() {
		return movable;
	}



	/*  ------  Methods called by the Listeners, may be overridden to add functionality  -------   */

	// Button was clicked by a Player
	// The Event is already cancelled and can be checked for clicktype etc.
	@Override
	public void click(InventoryClickEvent event, MenuView view) {
	}

	// Button was clicked by a Player, but he is not allowed to (canclick returned false)
	// The Event is already cancelled and can be checked for clicktype etc.
	@Override
	public void clickNotAllowed(InventoryClickEvent event, MenuView view) {
	}

	// Should return false if the Button should not be shown for the Player
	// Permission checks, etc can be done here
	@Override
	public boolean canSee(Player player, MenuView view) {
		return true;
	}

	// Should return false if the Button can not be clicked by the Player
	// Will call the method clickNotAllowed instead of click when clicked
	@Override
	public boolean canClick(Player player, MenuView view)  {
		return true;
	}

	// Returns the Delay that should be between clicks in miliseconds
	// If sameButton is true, the click was on the same button as before
	// So if multiple fast clicks on the same button should be avoided, do that if sameButton is true
	// Override this for buttons that should not be clicked often (maybe due to processing power)
	@Override
	public int getClickDelay(Player player, MenuView view, boolean sameButton) {
		return sameButton ? click1 : click2;
	}

	// Method that is called by the Menu to get the item to display
	// Returns the ItemStack that should display for this player
	// A StatedButton for example should override this to display a certain state for this player
	@Override
	public ItemStack getItem(Player player, MenuView view) {
		return getItem();
	}

	// Called when the Button is removed from the Menu
	@Override
	public void removing() {
	}

	// Called when the Button is being added to a Menu
	@Override
	public void adding(Menu menu, int slot) {
		setCurrentMenu(menu, slot);
	}


	/*   -------  Menu specific Methods  -------   */

	// Update this Button in a view for a player
	@Override
	public void update(MenuView view, Player player) {
		if (view.getMenu() != currentMenu) return;
		ItemStack update = getItem(player, view);
		Button[] buttons = currentMenu.getButtons();
		for (int i = 0; i < buttons.length; i++) {
			if (buttons[i] != null && buttons[i] == this) {
				view.getInventory().setItem(i, update);
			}
		}
	}

	@Override
	public Menu getCurrentMenu() {
		return currentMenu;
	}

	@Override
	public void setCurrentMenu(Menu menu, int slot) {
		currentMenu = menu;
	}


	/*   -------  Button changing Methods  -------   */

	@Override
	public String getName() {
		if (item.getItemMeta().hasDisplayName()) {
			return item.getItemMeta().getDisplayName();
		}
		return null;
	}

	@Override
	public void setName(String name) {
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(nameColor(name));
		item.setItemMeta(meta);
	}

	public void colorName(ChatColor color) {
		if (color != null) {
			ItemMeta meta = item.getItemMeta();
			if (meta.hasDisplayName()) {
				meta.setDisplayName(color + ChatColor.stripColor(meta.getDisplayName()));
				item.setItemMeta(meta);
			}
		}
	}

	@Override
	public Material getType() {
		return item.getType();
	}

	@Override
	public void setType(Material type) {
		item.setType(type);
	}

	@Override
	public short getDurability() {
		return item.getDurability();
	}

	@Override
	public void setDurability(short durability) {
		item.setDurability(durability);
	}

	@Override
	public List<String> getLore() {
		return item.getItemMeta().getLore();
	}

	@Override
	public void setLore(List<String> lore) {
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	// returns null if line doesnt exist
	@Override
	public String getLoreLine(int line) {
		if (!item.getItemMeta().hasLore()) {
			return null;
		}
		List<String> lore = item.getItemMeta().getLore();
		if (lore.size() <= line) {
			return null;
		}
		return lore.get(line);
	}

	@Override
	public void setLoreLine(int line, String loreLine) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		if (lore == null) {
			lore = new ArrayList<String>();
		}
		for (int i = lore.size(); i < line; i++) {
			lore.add("");
		}
		if (lore.size() == line) {
			lore.add(loreColor(loreLine));
		} else {
			lore.set(line, loreColor(loreLine));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	@Override
	public void addLore(String... lines) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		if (lore == null) {
			lore = new ArrayList<String>();
		}
		for (String line : lines) {
			lore.add(loreColor(line));
		}
		meta.setLore(lore);
		item.setItemMeta(meta);
	}

	@Override
	public void setGlowing(boolean glow) {
		item = NMSUtil.setGlowing(item, glow);
	}



	// Colors an item Name
	public static String nameColor(String name) {
		if (name == null) {
			return null;
		}
		if (name.equals(" ")) {
			return name;
		} else if (name.startsWith("&") || name.startsWith("§")) {
			return P.p.color(name);
		} else {
			return P.p.color("§f" + name);
		}
	}

	// Colors a line of lore
	public static String loreColor(String line) {
		if (line == null) {
			return null;
		}
		if (line.equals("")) {
			return line;
		} else if (line.startsWith("&") || line.startsWith("§")) {
			return P.p.color(line);
		} else {
			return P.p.color("§7" + line);
		}
	}

	@Override
	public int getAmount() {
		return item.getAmount();
	}

	@Override
	public void setAmount(int amount) {
		item.setAmount(amount);
	}

	// Returns the actual item representing the Button
	@Override
	public ItemStack getItem() {
		return item;
	}

	// Set the representing item
	// will not be set on currently opened menus, unless currentMenu.update() is called
	@Override
	public void setItem(ItemStack item) {
		this.item = item;
	}


}
