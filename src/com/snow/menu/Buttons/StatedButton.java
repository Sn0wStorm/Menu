package com.snow.menu.Buttons;


import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.snow.menu.Util.NMSUtil;

/*
  This is an extended Button that has more states than just one
  This could be Different names, glowing or just anything
  Upon initialization it has one additional state that is a copy of the main item
  The States are just additional to the main item, state 0 = first additional state
  This does not display any state on its own, it must be extended and
  the getItem(player) method overridden to show different states per player
*/

public class StatedButton extends Button {

	private ItemStack[] items = new ItemStack[1];

	public StatedButton(Material type) {
		super(type);
		items[0] = getItem().clone();
	}

	public StatedButton(Material type, short durability) {
		super(type, durability);
		items[0] = getItem().clone();
	}

	public StatedButton(Material type, String name) {
		super(type, name);
		items[0] = getItem().clone();
	}

	public StatedButton(Material type, short durability, String name) {
		super(type, durability, name);
		items[0] = getItem().clone();
	}

	public StatedButton(Material type, String name, String... lore) {
		super(type, name, lore);
		items[0] = getItem().clone();
	}

	public StatedButton(Material type, short durability, String name, String... lore) {
		super(type, durability, name, lore);
		items[0] = getItem().clone();
	}

	public StatedButton(ItemStack... items) {
		super(items[0]);
		if (items.length > 1) {
			this.items = Arrays.copyOfRange(items, 1, items.length);
		} else {
			this.items[0] = getItem().clone();
		}
	}

	// Will add the defined amount of items to the states
	// The added items are copies of the main item
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

	public ItemStack getState(int state) {
		return items[state];
	}

	public void setState(int state, ItemStack i) {
		if (items.length < state) {
			return;
		}
		if (items.length == state) {
			addStates(1);
		}
		items[state] = i;
	}

	public ItemStack[] getStateItems() {
		return items;
	}

	public void setStateItems(ItemStack[] i) {
		items = i;
	}

	public String getName(int state) {
		if (items[state].getItemMeta().hasDisplayName()) {
			return items[state].getItemMeta().getDisplayName();
		}
		return null;
	}

	public void setName(int state, String name) {
		ItemMeta meta = items[state].getItemMeta();
		meta.setDisplayName(nameColor(name));
		items[state].setItemMeta(meta);
	}

	public Material getType(int state) {
		return items[state].getType();
	}

	public void setType(int state, Material type) {
		items[state].setType(type);
	}

	public short getDurability(int state) {
		return items[state].getDurability();
	}

	public void setDurability(int state, short durability) {
		items[state].setDurability(durability);
	}

	public List<String> getLore(int state) {
		return items[state].getItemMeta().getLore();
	}

	public void setLore(int state, List<String> lore) {
		ItemMeta meta = items[state].getItemMeta();
		meta.setLore(lore);
		items[state].setItemMeta(meta);
	}

	public String getLoreLine(int state, int line) {
		ItemStack temp = getItem();
		setItem(items[state]);
		String re = super.getLoreLine(line);
		setItem(temp);
		return re;
	}

	public void setLoreLine(int state, int line, String loreLine) {
		ItemStack temp = getItem();
		setItem(items[state]);
		super.setLoreLine(line, loreLine);
		setItem(temp);
	}

	public void addLore(int state, String... lines) {
		ItemStack temp = getItem();
		setItem(items[state]);
		super.addLore(lines);
		setItem(temp);
	}

	public void setGlowing(int state, boolean glow) {
		items[state] = NMSUtil.setGlowing(items[state], glow);
	}

	public int getAmount(int state) {
		return items[state].getAmount();
	}

	public void setAmount(int state, int amount) {
		items[state].setAmount(amount);
	}
}
