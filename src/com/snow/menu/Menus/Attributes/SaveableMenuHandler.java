package com.snow.menu.Menus.Attributes;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.snow.menu.Saving.MUnknown;
import com.snow.menu.P;
import com.snow.menu.Saving.SqlButtonSaver;

public class SaveableMenuHandler {

	private static List<SaveableMenuHandler> menus = new ArrayList<SaveableMenuHandler>();

	private SaveableMenu menu;
	private FileConfiguration extra = new YamlConfiguration();

	public SaveableMenuHandler(SaveableMenu menu) {
		this.menu = menu;
	}

	public SaveableMenu getMenu() {
		return menu;
	}

	public FileConfiguration getExtra() {
		return extra;
	}

	public void setExtra(FileConfiguration extra) {
		this.extra = extra;
	}

	// Set if this menu should be globally saved or not
	public void setSavedSql(boolean save) {
		if (save) {
			menus.add(this);
		} else {
			menus.remove(this);
		}
	}

	public void remove() {
		menus.remove(this);
	}

	// Called when the save of the Menu starts
	// Returns whether this menu should be saved
	public boolean saveStart() {
		return true;
	}

	public String save_getName() {
		return menu.getName();
	}

	public String save_getClassName() {
		return menu.getClass().getName();
	}

	public int save_getSize() {
		return menu.getRows();
	}

	public FileConfiguration save_getExtra() {
		return extra;
	}

	public void saveDone() {
	}

	public void load_extra(FileConfiguration extra) {
		this.extra = extra;
	}

	public void load_done() {
		menu.load();
	}


	// This saves ALL Menus AND Buttons to Sql
	// and notifies other Servers that there is news
	// All Menus and Buttons need to have been setSaved(true) to be saved
	public static void saveSql() {
		SqlButtonSaver.saveTask();
	}

	// Load a menu from Sql or file
	// set setSaved to true if the menu should be saved to sql with next save
	public static SaveableMenu load(String name, String clazz, int size, String extra, boolean setSavedSql) {
		P.p.log("loading Menu: " + name + ", " + clazz + ", size: " + size);
		if (name == null || clazz == null) return null;
		try {
			Class<? extends SaveableMenu> menuClass = Class.forName(clazz).asSubclass(SaveableMenu.class);
			SaveableMenu menu = menuClass.getConstructor(String.class, int.class).newInstance(name, size);

			P.p.log("menu loaded! class: " + menu.getClass().getName());

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
			menu.getSaveHandler().load_extra(c);
			menu.getSaveHandler().load_done();

			return menu;
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			if (setSavedSql) {
				P.p.log("Loading as Unknown Menu");
				loadUnknown(name, clazz, size);
			}
		} catch (ClassCastException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void loadUnknown(String name, String type, int size) {
		MUnknown menu = new MUnknown(name, size);
		SaveableUnknownMenuHandler handler = new SaveableUnknownMenuHandler(menu, type);
		menu.setSaveHandler(handler);
		menus.add(handler);
	}

	public static List<SaveableMenuHandler> getMenus() {
		return menus;
	}
}
