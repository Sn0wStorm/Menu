package com.snow.menu.Menus.Attributes;


 /*
  * Stores a Class name and returns it instead of its own name while saving
  */

public class SaveableUnknownMenuHandler extends SaveableMenuHandler {

	private String clazz;

	public SaveableUnknownMenuHandler(SaveableMenu menu, String clazz) {
		super(menu);
		this.clazz = clazz;
	}

	@Override
	public String save_getClassName() {
		return clazz;
	}
}
