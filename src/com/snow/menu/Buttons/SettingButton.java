package com.snow.menu.Buttons;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.MenuView;


/*
  A Button that has a reference to a boolean (AtomicBoolean) stored for each player
  This Boolean is displayed as green or red item name and glowing or not
  The Boolean is inverted when the player clicks on the button
  As per Default the Button is not shown to players that have no stored setting
*/

public class SettingButton extends OnOffButton {

	private Map<UUID, AtomicBoolean> settings = new HashMap<UUID, AtomicBoolean>();
	public boolean showIfAbsent = false; // Should the button show for players that are not in the map

	public SettingButton(Material type) {
		super(type);
	}

	public SettingButton(Material type, short durability) {
		super(type, durability);
	}

	public SettingButton(Material type, String name) {
		super(type, name);
	}

	public SettingButton(Material type, short durability, String name) {
		super(type, durability, name);
	}

	public SettingButton(Material type, String name, String... lore) {
		super(type, name, lore);
	}

	public SettingButton(Material type, short durability, String name, String... lore) {
		super(type, durability, name, lore);
	}

	public SettingButton(ItemStack itemOn, ItemStack itemOff) {
		super(itemOn, itemOff);
	}


	public void putSetting(UUID player, AtomicBoolean b) {
		settings.put(player, b);
	}

	public void removeSetting(UUID player) {
		settings.remove(player);
	}

	public AtomicBoolean getSetting(UUID player) {
		return settings.get(player);
	}

	@Override
	public boolean canSee(Player player, MenuView view) {
		return (showIfAbsent || settings.containsKey(player.getUniqueId()));
	}

	@Override
	public ItemStack getItem(Player player, MenuView view) {
		AtomicBoolean b = settings.get(player.getUniqueId());
		if (b != null) {
			if (b.get()) {
				return getItem();
			}
		}
		return getState(0);
	}

	@Override
	public void click(InventoryClickEvent event, MenuView view) {
		Player p = (Player) event.getWhoClicked();
		AtomicBoolean b = settings.get(p.getUniqueId());
		if (b != null) {
			b.set(!b.get());
			update(view, p);
		}
	}
}
