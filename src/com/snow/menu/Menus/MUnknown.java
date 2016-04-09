package com.snow.menu.Menus;

import com.snow.menu.Menu;
import com.snow.menu.Menus.Attributes.SaveableMenu;
import com.snow.menu.Menus.Attributes.SaveableMenuHandler;

/*
 * Placeholder Menu if a Menu class is not found while loading
 * This should store the SavedUnknownMenuHandler which stores the old Class name
 */
public class MUnknown extends Menu implements SaveableMenu {

	private SaveableMenuHandler save;

	public MUnknown(String name, int size) {
		super(name, size);
	}

	@Override
	public SaveableMenuHandler getSaveHandler() {
		return save;
	}

	public void setSaveHandler(SaveableMenuHandler handler) {
		save = handler;
	}

	@Override
	public void load() {
	}
}
