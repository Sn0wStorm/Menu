package com.snow.menu.Menus.Attributes;

import com.snow.menu.IMenu;

public interface SaveableMenu extends IMenu {

	SaveableMenuHandler getSaveHandler();
	//void setSaveHandler(SaveableMenuHandler handler);

	// Method that gets called when the menu was initialized and loaded
	void onLoad();
}
