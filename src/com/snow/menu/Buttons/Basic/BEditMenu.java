package com.snow.menu.Buttons.Basic;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.Attributes.ImmovableButton;
import com.snow.menu.Buttons.OnOffButton;
import com.snow.menu.Menu;
import com.snow.menu.MenuView;
import com.snow.menu.Menus.Attributes.EditableMenu;
import com.snow.menu.Menus.MEdit;

public class BEditMenu extends OnOffButton implements ImmovableButton {

	public BEditMenu() {
		super(Material.BRICKS, "Menü Bearbeiten");
		getOn().setType(Material.BRICK);
	}

	public BEditMenu(Material type) {
		super(type);
	}

	public BEditMenu(Material type, short durability) {
		super(type, durability);
	}

	public BEditMenu(Material type, String name) {
		super(type, name);
	}

	public BEditMenu(Material type, short durability, String name) {
		super(type, durability, name);
	}

	public BEditMenu(Material type, String name, String... lore) {
		super(type, name, lore);
	}

	public BEditMenu(Material type, short durability, String name, String... lore) {
		super(type, durability, name, lore);
	}

	public BEditMenu(ItemStack itemOn, ItemStack itemOff) {
		super(itemOn, itemOff);
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
	public void click(InventoryClickEvent event, MenuView view) {
		if (view.getMenu() instanceof MEdit) {
			MEdit menu = ((MEdit) view.getMenu());
			menu.setEditing(event.getWhoClicked().getUniqueId(), !menu.isEditing(event.getWhoClicked().getUniqueId()));
			menu.update();
		}
	}

	@Override
	public ItemStack getItem(Player player, MenuView view) {
		if (currentMenu instanceof MEdit) {
			MEdit menu = ((MEdit) currentMenu);
			return getItem(menu.isEditing(player.getUniqueId()));
		}
		return getItem();
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
		setGlowing(true);
		initLoreDeAc();
	}

	@Override
	protected void initLoreDeAc() {
		addLore(0, "", "&8Klicken zum Bearbeiten");
		addLore("&aBearbeitungmodus", "", "&8Klicken um den Bearbeitungsmodus zu verlassen");
	}
}
