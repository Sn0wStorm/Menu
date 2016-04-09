package com.snow.menu.Buttons;

import java.io.StringReader;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.snow.menu.Buttons.Attributes.SaveableUnknownButtonHandler;
import com.snow.menu.Menus.Attributes.SaveableMenu;

/*
  GlobalButton that is used if the class of a loaded button can not be found
  This stores the original class and returns it instead of its own when saving
 */

public class UnknownButton extends GlobalButton {

	public UnknownButton(Material type, short durability, String name, String clazz) {
		super(type, durability, name);
		setSaveHandler(new SaveableUnknownButtonHandler(this, clazz));
		setSaved(true);
	}

	public void load(SaveableMenu menu, int slot, String extra, String text, int buttonType) {
		getSaveHandler().load_menu(menu, slot);

		FileConfiguration c = null;
		if (extra != null) {
			try {
				c = YamlConfiguration.loadConfiguration(new StringReader(extra));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (c == null) {
			c = new YamlConfiguration();
		}

		getSaveHandler().load_extra(c);
		getSaveHandler().load_text(text);
		getSaveHandler().load_buttonType(buttonType);
	}

}
