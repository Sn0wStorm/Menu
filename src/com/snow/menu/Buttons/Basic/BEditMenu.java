package com.snow.menu.Buttons.Basic;

import com.snow.menu.Buttons.Attributes.ImmovableButton;
import com.snow.menu.Buttons.OnOffButton;
import com.snow.menu.Menu;
import com.snow.menu.MenuView;
import com.snow.menu.Menus.Attributes.EditableMenu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

public class BEditMenu extends OnOffButton implements ImmovableButton {

	public static final int EDITING = 0;
	public static final int EDIT = 1;
	public static final int HIDDEN = 2;

	public BEditMenu() {
		super(Material.BRICK, "Menü Bearbeiten");
		setType(EDIT, Material.BRICKS);
		addStates(1);
		setItem(HIDDEN, new BEmptyTopTile().getItem());
	}

	@Override
	public int getClickDelay(Player player, MenuView view, boolean sameButton) {
		return sameButton ? 800 : 300;
	}

	// Add This Button to the default slot in the menu
	public BEditMenu addToMenu(Menu menu) {
		menu.addButton(this, getDefaultSlot());
		return this;
	}

	public static int getDefaultSlot() {
		return 6;
	}

	@Override
	public void onClick(InventoryClickEvent event, MenuView view) {
		if (view.getMenu() instanceof EditableMenu) {
			EditableMenu menu = ((EditableMenu) view.getMenu());
			if (!menu.canEdit(((Player) event.getWhoClicked()))) {
				return;
			}
			menu.setEditing(event.getWhoClicked().getUniqueId(), !menu.isEditing(event.getWhoClicked().getUniqueId()));
			menu.update();
		}
	}

	@Override
	public int showState(Player player, MenuView view) {
		Menu menu = view.getMenu();
		if (menu instanceof EditableMenu) {
			if (!((EditableMenu) menu).canEdit(player)) {
				return HIDDEN;
			} else {
				return ((EditableMenu) menu).isEditing(player.getUniqueId()) ? EDITING : EDIT;
			}
		} else {
			return menu.canAdmin(player) ? EDIT : HIDDEN;
		}
	}

	@Override
	public boolean shouldShowOn(Player player, MenuView view) {
		// Never called as we override the showState() Method
		return false;
	}

	/*@Override
	public boolean shouldShowOn(Player player, MenuView view) {
		if (currentMenu instanceof EditMenu) {
			EditMenu menu = ((EditMenu) currentMenu);
			return menu.isEditing(player.getUniqueId());
		}
		return OFF;
	}

	@Override
	public boolean canSee(Player player, MenuView view) {
		if (view.getMenu() instanceof EditableMenu) {
			return ((EditableMenu) view.getMenu()).canEdit(player);
		}
		return view.getMenu().canAdmin(player);
	}

	@Override
	protected void initOnOff() {
		setGlowing(0, true);
		initLoreDeAc();
	}*/

	@Override
	protected void initLoreDeAc() {
		addLore(1, "", "&8Klicken zum Bearbeiten");
		addLore(0, "&aBearbeitungmodus", "", "&8Klicken um den Bearbeitungsmodus zu verlassen");
	}
}
