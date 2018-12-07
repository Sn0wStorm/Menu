package com.snow.menu.Buttons;

import com.snow.menu.MenuView;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*
  A Stated Button with only one additional state: off
  Will always have the two items getItem() and getItem(0)
  The Main item will have a green name, the other a red name
  Lore depending on the on/off state will be added

  Need to override getItem() to return either getOn() or getOff()
  return condition ? getOn() : getOff();
*/

public abstract class OnOffButton extends StatedButton {

	public OnOffButton(Material type) {
		super(type);
		initOnOff();
	}

	public OnOffButton(Material type, short durability) {
		super(type, durability);
		initOnOff();
	}

	public OnOffButton(Material type, String name) {
		super(type, name);
		initOnOff();
	}

	public OnOffButton(Material type, short durability, String name) {
		super(type, durability, name);
		initOnOff();
	}

	public OnOffButton(Material type, String name, String... lore) {
		super(type, name, lore);
		initOnOff();
	}

	public OnOffButton(Material type, short durability, String name, String... lore) {
		super(type, durability, name, lore);
		initOnOff();
	}

	public OnOffButton(ItemStack itemOn, ItemStack itemOff) {
		super(itemOn, itemOff);
	}


	public ItemStack getOn() {
		return getItem();
	}

	public ItemStack getOff() {
		return getState(0);
	}

	public ItemStack getItem(boolean on) {
		return on ? getOn() : getOff();
	}

	@Override
	public abstract ItemStack getItem(Player player, MenuView view);

	protected void initOnOff() {
		String name = getName();
		if (name == null) {
			name = getType().name();
		}
		name = ChatColor.stripColor(name);
		setName("&a" + name);
		setName(0, "&c" + name);
		setGlowing(true);
		initLoreDeAc();
	}

	protected void initLoreDeAc() {
		addLore("", "&8Klicken zum deaktivieren");
		addLore(0, "", "&8Klicken zum aktivieren");
	}

	protected void initLoreOnOff() {
		addLore("", "&8Klicken zum Auschalten");
		addLore(0, "", "&8Klicken zum Einschalten");
	}
}
