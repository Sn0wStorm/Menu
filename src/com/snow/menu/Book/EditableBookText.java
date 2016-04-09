package com.snow.menu.Book;


import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.snow.menu.P;

public class EditableBookText extends BookText {

	public EditableBookText() {
		meta = ((BookMeta) P.p.getServer().getItemFactory().getItemMeta(Material.BOOK_AND_QUILL));
		meta.setDisplayName("§fRechtsklick");
	}


	// Puts the Book into the players hand. He then has to rightclick to open it!!
	@Override
	public void displayTo(Player player) {
		player.closeInventory();
		ItemStack book = new ItemStack(Material.BOOK_AND_QUILL);
		if (meta.getPageCount() < 1) {
			meta.addPage("");
		}
		book.setItemMeta(meta);
		new ItemReplacer(book, player, this);
	}

	// Player has edited this booktext
	// Event is already cancelled and the book removed from the players inv
	// pages is a List of all new pages, cleaned from "§0" the meta tends to add at beginning and end of lines
	public void onEdit(PlayerEditBookEvent event, List<String> pages) {

	}

	// Player did not properly open the book to edit, so it had to be removed
	// Use this to do stuff if editing failed, like opening the last menu
	// hadOpen is true if the player had opened the Book to edit at least once, but pressed esc
	public void wasRemoved(Player player, boolean hadOpen) {

	}
}
