package com.snow.menu.Buttons;

import com.snow.menu.MenuView;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*
  A Stated Button with only one additional state: off
  Will always have the two items getItem(0) and getItem(1)
  The Main item will have a green name, the other a red name
  Lore depending on the on/off state will be added

  Need to Override shouldShowOn to return which button to show
*/

public abstract class OnOffButton extends MultiStateButton {

	public static final boolean ON = true;
	public static final boolean OFF = false;

	public OnOffButton(Material type) {
		super(type);
		initOnOff();
	}

	public OnOffButton(Material type, String name) {
		super(type, name);
		initOnOff();
	}

	public OnOffButton(Material type, String name, String... lore) {
		super(type, name, lore);
		initOnOff();
	}

	public OnOffButton(ItemStack itemOn, ItemStack itemOff) {
		super(itemOn, itemOff);
	}


	public ItemStack getOn() {
		return getItem(0);
	}

	public ItemStack getOff() {
		return getItem(1);
	}

	public ItemStack getItem(boolean on) {
		return on ? getOn() : getOff();
	}

	public String getOnName() {
		return "An";
	}

	public String getOffName() {
		return "Aus";
	}

	@Override
	public int showState(Player player, MenuView view) {
		return shouldShowOn(player, view) ? 0 : 1;
	}

	// Return true if the ON button should be showed to the Player
	// Return false if the OFF button ...
	public abstract boolean shouldShowOn(Player player, MenuView view);

	// Methods below can be overridden to initialize different On Off Names and Lore

	protected void initOnOff() {
		String name = getName();
		if (name == null) {
			name = getType().name();
		}
		name = ChatColor.stripColor(name);
		setName(0, name + " §7[§a"+getOnName()+"§7]");
		setName(1, name + " §7[§c"+getOffName()+"§7]");
		setGlowing(0, true);
		initLoreDeAc();
	}

	protected void initLoreDeAc() {
		addLore(0, "", "&8Klicken zum deaktivieren");
		addLore(1, "", "&8Klicken zum aktivieren");
	}

	protected void initLoreOnOff() {
		addLore(0, "", "&8Klicken zum Auschalten");
		addLore(1, "", "&8Klicken zum Einschalten");
	}
}
