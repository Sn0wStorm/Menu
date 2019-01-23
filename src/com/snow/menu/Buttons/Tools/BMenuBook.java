package com.snow.menu.Buttons.Tools;

import java.util.ArrayList;
import java.util.List;

import com.snow.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.snow.menu.Buttons.Button;
import com.snow.menu.MenuView;
import com.snow.menu.P;

public class BMenuBook extends Button {

	public BMenuBook() {
		super(Material.BOOK, "Menü Buch",
				"Du hast dein Menü buch verloren?", "Hier bekommst du ein neues!",
				"Du kannst aber auch /menü benutzen.",
				"", "§8Klick um ein Menü Buch zu erhalten");
	}

	public BMenuBook(String name, String... lore) {
		super(Material.BOOK, name, lore);
	}

	@Override
	public void onClick(InventoryClickEvent event, MenuView view) {
		Player player = (Player) event.getWhoClicked();
		if (!player.hasPermission("menu.getbook")) {
			P.p.msg(player, "&cDu hast keine Rechte dazu");
			return;
		}

		for (ItemStack item : player.getInventory().getContents()) {
			if (item == null) continue;
			if (isMenuBook(item)) {
				P.p.msg(player, "§cDu hast bereits ein Menü Buch!");
				return;
			}
		}

		ItemStack mb = new ItemStack(Material.BOOK);
		ItemMeta meta = P.p.getServer().getItemFactory().getItemMeta(Material.BOOK);
		meta.setDisplayName("§7Menü Buch");
		List<String> lore = new ArrayList<String>();
		lore.add("");
		lore.add("§8Rechtsklick um das Menü zu öffnen");
		meta.setLore(lore);
		mb.setItemMeta(meta);
		player.getInventory().addItem(mb);
		P.p.msg(player, "&aMenü Buch erhalten");
	}

	@Override
	public int getClickDelay(Player player, MenuView view, boolean sameButton) {
		return sameButton ? 10000 : 150;
	}

	public static boolean isMenuBook(ItemStack item) {
		if (item.getType().equals(Material.BOOK)) {
			if (item.hasItemMeta()) {
				ItemMeta meta = item.getItemMeta();
				if (meta != null && meta.hasDisplayName() && meta.getDisplayName().equals("§7Menü Buch")) {
					if (item.getAmount() > 1) {
						item.setAmount(1);
					}
					return true;
				}
			}
		}
		return false;
	}
}
