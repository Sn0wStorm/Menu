package com.snow.menu.Buttons.Test;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import com.snow.mkremins.fanciful.FancyMessage;

import com.snow.menu.Book.BookButton;
import com.snow.menu.Book.BookText;
import com.snow.menu.Buttons.Button;
import com.snow.menu.MenuView;

public class BBookText extends Button {

	private static FancyMessage msg;

	public BBookText(Material type, String name) {
		super(type, name);
	}


	@Override
	public void click(InventoryClickEvent event, MenuView view) {
		if (msg == null) {
			msg = new FancyMessage("Dies ist hoffentlich ein gutes Beispiel für einen Menü Dialog\n\n");
			msg.color(ChatColor.RED);
		}

		try {
			FancyMessage m = msg.clone();
			BookText text = new BookText();

			BookButton byes = new BookButton(event.getWhoClicked().getUniqueId(), text) {
				@Override
				public boolean click(Player player) {
					player.sendTitle("§aVerstanden", "Du hast verstanden");
					return false;
				}
			};

			BookButton bno = new BookButton(event.getWhoClicked().getUniqueId(), text) {
				@Override
				public boolean click(Player player) {
					player.sendTitle("§4Nicht verstasnden", "Du hast nicht verstanden");
					return true;
				}
			};

			m.then(byes.create("Verstanden", true, null, "Ok, ich habe verstanden"));
			m.then("  ");
			m.then(bno.create("Nope", true, ChatColor.RED, "Ne, ich hab nicht verstanden"));

			byes.register();
			bno.register();

			event.getWhoClicked().closeInventory();
			text.setText(m);
			text.displayTo(((Player) event.getWhoClicked()));

		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
}
