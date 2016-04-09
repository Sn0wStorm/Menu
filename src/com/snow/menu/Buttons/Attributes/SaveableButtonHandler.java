package com.snow.menu.Buttons.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.snow.menu.Menus.Attributes.SaveableMenu;
import com.snow.menu.Menus.Attributes.SaveableMenuHandler;

public class SaveableButtonHandler {

	private SaveableButton b;
	private FileConfiguration extra = new YamlConfiguration();

	public SaveableButtonHandler(SaveableButton b) {
		setButton(b);
	}

	public void setButton(SaveableButton b) {
		this.b = b;
	}

	public SaveableButton getButton() {
		return b;
	}

	public FileConfiguration getExtra() {
		return extra;
	}

	public void setExtra(FileConfiguration extra) {
		this.extra = extra;
	}

	// Called when the save is started
	// Return whether this button should be saved
	public boolean saveStart() {
		return true;
	}

	// The Display Name of the Button as String
	public String save_getName() {
		return getButton().getName();
	}

	// Bukkit Material of the Button
	public String save_getType() {
		return getButton().getType().toString();
	}

	// Bukkit Durability of the Button
	public short save_getDurability() {
		return getButton().getDurability();
	}

	// ItemStack amount of the Button
	public int save_getAmount() {
		return getButton().getAmount();
	}

	// ID of the Menu this Button is in
	public int save_getMenuId() {
		if (!(getButton().getCurrentMenu() instanceof SaveableMenu)) return -1;
		for (int i = 0; i < SaveableMenuHandler.getMenus().size(); i++) {
			if (SaveableMenuHandler.getMenus().get(i).getMenu() == getButton().getCurrentMenu()) {
				return i;
			}
		}
		return -1;
	}

	// Slot this button is in
	public int save_getSlot() {
		return 0;
	}

	// Extra data like menu to open or commands to run
	// Stored in a FileConfiguration
	public FileConfiguration save_getExtra() {
		return extra;
	}

	// Some integer that represents some type of button
	public int save_getButtonType() {
		return 0;
	}

	// The Name of The Button Class
	public String save_getClassName() {
		return getButton().getClass().getName();
	}

	// Text, like lore etc.
	public String save_getText() {
		StringBuilder builder = new StringBuilder();
		for (String line : getButton().getLore()) {
			if (line != null) {
				builder.append(line).append("\n");
			}
		}
		if (builder.length() > 2) {
			return builder.substring(0, builder.length() - 2);
		} else {
			return "";
		}
	}

	// Called when the save is done
	public void saveDone() {
	}

	public void load_amount(int amount) {
		getButton().setAmount(amount);
	}

	public void load_buttonType(int buttonType) {
	}

	public void load_menu(SaveableMenu menu, int slot) {
	}

	public void load_text(String text) {
		if (text == null || text.length() < 1) {
			return;
		}
		List<String> lore = new ArrayList<String>();
		Collections.addAll(lore, text.split("\n"));
		if (!lore.isEmpty()) {
			getButton().setLore(lore);
		}
	}

	public void load_extra(FileConfiguration extra) {
		this.extra = extra;
	}

	public void loadDone() {
		getButton().load();
	}
}
