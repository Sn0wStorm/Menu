package com.snow.testmenu.Buttons;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.snow.menu.Buttons.Button;
import com.snow.menu.Menu;
import com.snow.menu.MenuView;

public class BTestSettings extends Button {

	public static Menu settingsMenu;

	public BTestSettings() {
		super(Material.COMPARATOR, "Einstellungen");
	}

	@Override
	public void onClick(InventoryClickEvent event, MenuView view) {
		settingsMenu.show((Player) event.getWhoClicked());
	}

	public static void init() {
		settingsMenu = new Menu("Einstellungen", 6);
		settingsMenu.addButton(new Button(Material.OBSIDIAN), 2, 4);
		settingsMenu.addButton(new Button(Material.LAVA_BUCKET, null, "", "&cHot"), 1, 4);
		settingsMenu.addButton(new Button(Material.LAVA_BUCKET, null, "", "&cHot"), 2, 5);
		settingsMenu.addButton(new Button(Material.LAVA_BUCKET, null, "", "&cHot"), 3, 4);
		settingsMenu.addButton(new Button(Material.LAVA_BUCKET, null, "", "&cHot"), 2, 3);

		/*for (Button button : settingsMenu.getButtons()) {
			if (button != null) {
				if (button.getType().equals(Material.STATIONARY_LAVA)) {
					button.setLoreLine(1, "&cHot");
				}
			}
		}*/
		settingsMenu.showBasicButtons(true);
	}
}
