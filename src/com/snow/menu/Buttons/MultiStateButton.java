package com.snow.menu.Buttons;


import com.snow.menu.MenuView;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/*
  This is an extended Button that has more states than just one
  This could be Different names, glowing or just anything
  Upon initialization it has one additional state that is a copy of the given
  State 0 = Main Item, State 1 = another Item, etc...
  (items[0] is always null as that is the main state and stored in the Button class)
  The showState Method has to be overridden to return which state to display to the Player
*/

public abstract class MultiStateButton extends Button {

	private ItemStack[] items = new ItemStack[2];

	public MultiStateButton(Material type) {
		super(type);
		items[1] = getItem().clone();
	}

	public MultiStateButton(Material type, String name) {
		super(type, name);
		items[1] = getItem().clone();
	}

	public MultiStateButton(Material type, String name, String... lore) {
		super(type, name, lore);
		items[1] = getItem().clone();
	}

	public MultiStateButton(ItemStack... items) {
		super(items[0]);
		if (items.length > 1) {
			this.items = items;
		} else {
			this.items[1] = getItem().clone();
		}
	}

	// Return which state of the Button to show to this Player
	public abstract int showState(Player player, MenuView view);


	// Will add the defined amount of items to the states
	// The added items are copies of the first item
	// returns amount of states after finished
	public int addStates(int num) {
		if (num < 1) return items.length;
		int oldLength = items.length;
		items = Arrays.copyOf(items, items.length + num);
		for (; oldLength < items.length; oldLength++) {
			items[oldLength] = getItem().clone();
		}
		return items.length;
	}

	public int getNumStates() {
		return items.length;
	}

	public ItemStack getItem(int state) {
		if (state == 0) {
			return getItem();
		}
		return items[state];
	}

	// Set any of the States to a new item. 0 = Main State
	public void setItem(int state, ItemStack i) {
		if (state == 0) {
			setItem(i);
			return;
		}
		if (items.length < state) {
			return;
		}
		if (items.length == state) {
			addStates(1);
		}
		items[state] = i;
	}

	// Called when showing in a menu. We choose a state by calling showState()
	@Override
	public ItemStack getItem(Player player, MenuView view) {
		return getItem(showState(player, view));
	}

	// Get The Array of Items that represent the additional states to the first item
	// The 0-entry is null as that item can be retrieved with getItem()
	public ItemStack[] getAdditionalStateItems() {
		return items;
	}

	// The Main item has to be set with setItem()
	public void setAdditionalStateItems(ItemStack[] i) {
		items = i;
	}

	public String getName(int state) {
		if (state == 0) {
			return getName();
		}
		if (items[state].getItemMeta().hasDisplayName()) {
			return items[state].getItemMeta().getDisplayName();
		}
		return null;
	}

	public void setName(int state, String name) {
		if (state == 0) {
			setName(name);
			return;
		}
		ItemMeta meta = items[state].getItemMeta();
		meta.setDisplayName(nameColor(name));
		items[state].setItemMeta(meta);
	}

	public Material getType(int state) {
		if (state == 0) {
			return getType();
		}
		return items[state].getType();
	}

	public void setType(int state, Material type) {
		if (state == 0) {
			setType(type);
			return;
		}
		items[state].setType(type);
	}

	public List<String> getLore(int state) {
		if (state == 0) {
			return getLore();
		}
		return items[state].getItemMeta().getLore();
	}

	public void setLore(int state, List<String> lore) {
		if (state == 0) {
			setLore(lore);
			return;
		}
		ItemMeta meta = items[state].getItemMeta();
		meta.setLore(lore);
		items[state].setItemMeta(meta);
	}

	public String getLoreLine(int state, int line) {
		if (state == 0) {
			return getLoreLine(line);
		}
		ItemStack item = items[state];
		ItemStack temp = getItem();
		setItem(item);
		String re = super.getLoreLine(line);
		setItem(temp);
		return re;
	}

	public void setLoreLine(int state, int line, String loreLine) {
		if (state == 0) {
			setLoreLine(line, loreLine);
			return;
		}
		ItemStack item = items[state];
		ItemStack temp = getItem();
		setItem(item);
		super.setLoreLine(line, loreLine);
		setItem(temp);
	}

	public void addLore(int state, String... lines) {
		if (state == 0) {
			addLore(lines);
			return;
		}
		ItemStack item = items[state];
		ItemStack temp = getItem();
		setItem(item);
		super.addLore(lines);
		setItem(temp);
	}

	public void setGlowing(int state, boolean glow) {
		if (state == 0) {
			setGlowing(glow);
			return;
		}
		ItemStack item = items[state];
		ItemStack temp = getItem();
		setItem(item);
		super.setGlowing(glow);
		setItem(temp);
	}

	public int getAmount(int state) {
		if (state == 0) {
			return getAmount();
		}
		return items[state].getAmount();
	}

	public void setAmount(int state, int amount) {
		if (state == 0) {
			setAmount(amount);
			return;
		}
		items[state].setAmount(amount);
	}
}
