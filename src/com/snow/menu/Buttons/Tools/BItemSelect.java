package com.snow.menu.Buttons.Tools;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.Button;
import com.snow.menu.MenuView;

public class BItemSelect extends Button {

	private static Map<UUID, Selector> selectors = new HashMap<UUID, Selector>();

	public BItemSelect(Material type) {
		super(type);
	}

	public BItemSelect(Material type, String name) {
		super(type, name);
	}

	public BItemSelect(Material type, String name, String... lore) {
		super(type, name, lore);
	}

	public BItemSelect(ItemStack item) {
		super(item);
	}

	@Override
	public void click(InventoryClickEvent event, MenuView view) {
		Player player = ((Player) event.getWhoClicked());
		Selector s = selectors.get(player.getUniqueId());
		if (s != null) {
			removeSelector(player.getUniqueId());
			s.selected(player.getUniqueId(), this);
			view.getBackMenu().showAgain(player, view);
		}
	}

	public static void useSelector(UUID player, Selector selector) {
		selectors.put(player, selector);
	}

	public static void removeSelector(UUID player) {
		selectors.remove(player);
	}

	public static Selector getSelector(UUID player) {
		return selectors.get(player);
	}
}
