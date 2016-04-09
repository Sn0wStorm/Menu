package com.snow.menu.Menus.Test;

import org.bukkit.Material;

import com.snow.menu.Buttons.GlobalButton;
import com.snow.menu.Buttons.GlobalEditableButton;
import com.snow.menu.Menus.MEdit;
import com.snow.menu.P;

public class MEditTest extends MEdit {
	public MEditTest(String name, int size) {
		super(name, size);
		showBasicButtons(true);
		getSaveHandler().setSavedSql(true);
	}

	@Override
	public void load() {
		P.p.mainMenu.editTest.setMenuToShow(this);
	}

	public MEditTest init() {
		GlobalEditableButton b = new GlobalEditableButton(Material.PACKED_ICE, "ZLAT", "Na da bin ich mal gespannt");
		b.getEditHandler().initFromLore();
		addButton(b, 1, 0);
		addButton(new GlobalButton(Material.YELLOW_FLOWER, "Die Gelbe", "", "Und die bleibt wie sie ist!"), 1, 3);
		return this;
	}
}
