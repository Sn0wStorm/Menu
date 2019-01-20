package com.snow.menu.Buttons;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.Attributes.EditableButton;
import com.snow.menu.Buttons.Attributes.SaveableButton;
import com.snow.menu.Buttons.Attributes.SaveableButtonHandler;
import com.snow.menu.Buttons.Attributes.SaveableGlobalButtonHandler;
import com.snow.menu.Menu;
import com.snow.menu.Saving.SqlButtonSaver;

  /*
    Button that is Globally Saved to MySQL
    Add to a Menu or call setSaved(true) to mark for being saved
    Start the Savetask for all Buttons and Menus by calling save()
  */

public class GlobalButton extends Button implements SaveableButton {

	private static Set<GlobalButton> buttons = new HashSet<>();

	private int slot;
	private SaveableGlobalButtonHandler save = new SaveableGlobalButtonHandler(this);

	public GlobalButton(Material type) {
		super(type);
	}

	public GlobalButton(Material type, String name) {
		super(type, name);
	}

	public GlobalButton(Material type, String name, String... lore) {
		super(type, name, lore);
	}

	public GlobalButton(ItemStack item) {
		super(item);
	}

	@Override
	public SaveableButtonHandler getSaveHandler() {
		return save;
	}

	@Override
	public void onLoad() {
	}

	@Override
	public void setSaveHandler(SaveableButtonHandler save) {
		if (!(save instanceof SaveableGlobalButtonHandler))
			throw new IllegalArgumentException("Illegal SaveHandler for GlobalButton");
		this.save = ((SaveableGlobalButtonHandler) save);
	}

	// Can be overridden to use custom button types
	public int getSqlButtonType() {
		if (this instanceof EditableButton) {
			return ((EditableButton) this).getEditHandler().nameEditable ? 1 : 0;
		}
		return 0;
	}

	// The button has to be added to a menu or this has to be called to save the button globally to sql
	public void setSaved(boolean save) {
		if (save) {
			buttons.add(this);
		} else {
			buttons.remove(this);
		}
	}

	@Override
	public void onRemoving() {
		buttons.remove(this);
	}

	@Override
	public void onAdding(Menu menu, int slot) {
		super.onAdding(menu, slot);
		buttons.add(this);
	}

	public int getSlot() {
		return slot;
	}

	public void load_slot(int slot) {
		this.slot = slot;
	}

	@Override
	public void setCurrentMenu(Menu menu, int slot) {
		super.setCurrentMenu(menu, slot);
		this.slot = slot;
	}

	// This saves ALL Menus AND Buttons to Sql
	// and notifies other Servers that there is news
	// All Menus and Buttons need to have been setSaved(true) to be saved
	public static void save() {
		SqlButtonSaver.saveTask();
	}

	public static Set<GlobalButton> getButtons() {
		return buttons;
	}
}
