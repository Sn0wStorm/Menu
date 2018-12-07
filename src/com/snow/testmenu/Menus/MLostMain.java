package com.snow.testmenu.Menus;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.snow.menu.Buttons.Basic.BEmptyTile;
import com.snow.testmenu.Buttons.BLostSettings;
import com.snow.menu.Buttons.SubMenu.BShowMenu;
import com.snow.menu.Buttons.Test.BBookText;
import com.snow.menu.Buttons.Test.BTest;
import com.snow.menu.Buttons.Test.BTestItemSelect;
import com.snow.menu.Buttons.Tools.BMenuBook;
import com.snow.menu.Menu;
import com.snow.menu.Menus.Attributes.EditableMenu;
import com.snow.menu.Menus.Attributes.EditableMenuHandler;
import com.snow.menu.Menus.Test.MEditTest;
import com.snow.menu.P;

public class MLostMain extends Menu implements EditableMenu {

	private EditableMenuHandler edit;
	public BShowMenu editTest;
	//private SaveableMenuHandler save;

	public MLostMain() {
		super("Hauptmenü", 6);
		edit = new EditableMenuHandler(this);
		//save = new SaveableMenuHandler(this);
		//save.setSavedSql(true);

		showBasicButtons(true);
		addButton(new BTest(Material.BLAZE_POWDER, "Heyho test"), 1, 3);
		//addButton(new BEditText("§6PiPaPotato"), 2, 6);
		addButton(new BLostSettings(), 3, 4);
		addButton(new BMenuBook(), 3, 0);
		addButton(new BTestItemSelect(Material.EMERALD_ORE), 3, 5);
		editTest = new BShowMenu(Material.DIAMOND_AXE, "Bearbeitungs Test", "ZLAT");
		addButton(editTest, 5, 3);
		P.p.getServer().getScheduler().runTaskLater(P.p, new Runnable() {
			@Override
			public void run() {
				if (editTest.getMenuToShow() == null) {
					editTest.setMenuToShow(new MEditTest("Bearbeitungs Test", 6).init());
				}
			}
		}, 10);
		addButton(new BEmptyTile(), 4, 8);
		addButton(new BBookText(Material.BOOK, "Ein Menüdialog im Buch"), 4, 6);
		BLostSettings.init();
	}

	@Override
	public EditableMenuHandler getEditHandler() {
		return edit;
	}

	/*@Override
	public SaveableMenuHandler getSaveHandler() {
		return save;
	}

	@Override
	public void setSaveHandler(SaveableMenuHandler handler) {
		save = handler;
	}

	@Override
	public void load() {

	}*/

	@Override
	public boolean canEdit(Player player) {
		return canAdmin(player);
	}
}
