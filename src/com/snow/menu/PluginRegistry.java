package com.snow.menu;

import com.snow.menu.Buttons.Button;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PluginRegistry {

	private Map<NamespacedKey, Supplier<Button>> buttons = new HashMap<>();
	private Map<NamespacedKey, Menu> menus = new HashMap<>();


	public void registerButton(Plugin plugin, String name, Supplier<Button> button) {
		registerButton(new NamespacedKey(plugin, name), button);
	}

	public void registerButton(NamespacedKey key, Supplier<Button> button) {
		buttons.put(key, button);
	}

	public Button getButton(Plugin plugin, String name) {
		return getButton(new NamespacedKey(plugin, name));
	}

	public Button getButton(NamespacedKey key) {
		return buttons.get(key).get();
	}


	public void registerMenu(Plugin plugin, String name, Menu menu) {
		registerMenu(new NamespacedKey(plugin, name), menu);
	}

	public void registerMenu(NamespacedKey key, Menu menu) {
		menus.put(key, menu);
	}

	public Menu getMenu(Plugin plugin, String name) {
		return getMenu(new NamespacedKey(plugin, name));
	}

	public Menu getMenu(NamespacedKey key) {
		return menus.get(key);
	}

}
