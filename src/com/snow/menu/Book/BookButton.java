package com.snow.menu.Book;

import java.util.Random;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.snow.mkremins.fanciful.FancyMessage;

public abstract class BookButton {

	public static ClickListener clickListener;

	private UUID player;
	private String id;
	private BookText text;

	public BookButton(UUID player, BookText text) {
		this.text = text;
		this.player = player;
		generateId();
	}

	// Will create the FancyMessage that can be used in a BookText
	// Clicking this text will run the onClick(player) method
	// color == null makes it blue, tooltip == null no tooltip
	public FancyMessage create(String text, boolean underlined, ChatColor color, String... tooltip) {
		FancyMessage msg = new FancyMessage(text);
		if (tooltip != null) {
			msg.tooltip(tooltip);
		}
		if (underlined) {
			msg.style(ChatColor.UNDERLINE);
		}
		if (color == null) {
			msg.color(ChatColor.BLUE);
		} else {
			msg.color(color);
		}
		msg.command("/bcls " + (id == null ? "NoCommand" : id));
		return msg;
	}

	public void register() {
		clickListener.addButton(this);
	}


	public UUID getPlayer() {
		return player;
	}

	public String getId() {
		return id;
	}

	public BookText getText() {
		return text;
	}

	protected void generateId() {
		StringBuilder b = new StringBuilder(5);
		Random r = new Random();
		for (int i = 0; i < 5; i++) {
			b.append((char) (r.nextInt(26) + 'a'));
		}
		id = b.toString();
	}

	// Called when the Button is clicked by the Player.
	// The book will always close on click
	// Should return whether to reopen the book or not
	// If returns true, the Buttons will not be removed from the listener
	// If return false, all Buttons for the Player will be removed from the listener
	public abstract boolean onClick(Player player);
}
