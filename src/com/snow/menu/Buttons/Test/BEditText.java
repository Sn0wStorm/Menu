package com.snow.menu.Buttons.Test;


import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.GlobalEditableButton;
import com.snow.menu.MenuView;
import com.snow.menu.P;

public class BEditText extends GlobalEditableButton {

	public BEditText(String name) {
		super(Material.BAKED_POTATO, name);

		getEditHandler().nameEditable = false;
		List<String> text = new ArrayList<String>(6);

		text.add("&7Diese Kartoffel ist &6magisch");
		text.add("&7Nicht nur weil sie in diesem Menü ist...");
		text.add("&7Sie ist editierbar!");
		text.add("&7Und hat mehrere Seiten ->");
		text.add(null);
		text.add("&fTeste es jetzt :D!");
		getEditHandler().setText(text);

		setSaved(true);
	}

	@Override
	public void onClick(InventoryClickEvent event, MenuView view) {
		P.p.msg(event.getWhoClicked(), "Yesss edit me!");

		if (event.getClick().equals(ClickType.RIGHT) || event.getClick().equals(ClickType.SHIFT_RIGHT)) {
			getEditHandler().edit((Player) event.getWhoClicked(), view);
		}
	}

	@Override
	public ItemStack getItem(Player player, MenuView view) {
		return super.getItem(player, view);
	}
}
