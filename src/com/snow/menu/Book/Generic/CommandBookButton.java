package com.snow.menu.Book.Generic;

import org.bukkit.entity.Player;

import com.snow.menu.Book.BookButton;
import com.snow.menu.Book.BookText;

public class CommandBookButton extends BookButton {

	private String cmd;
	private boolean reopen;

	// The Command to execute by the player when clicking.
	// Should include the / or else the player sends a chat message
	public CommandBookButton(Player player, BookText text, String command, boolean reopenBook) {
		super(player.getUniqueId(), text);
		cmd = command;
		reopen = reopenBook;
	}

	@Override
	public boolean onClick(Player player) {
		player.performCommand(cmd);
		return reopen;
	}
}
