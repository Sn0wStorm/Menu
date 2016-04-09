package com.snow.menu.Book.Generic;

import org.bukkit.entity.Player;

import com.snow.menu.Book.BookButton;
public class CloseBookButton extends BookButton {

	public CloseBookButton() {
		super(null, null);
	}

	@Override
	public boolean click(Player player) {
		return false;
	}

	@Override
	public void register() {
		throw new UnsupportedOperationException("CloseBookButton can not be registered into the listener, as it has no function");
	}

	@Override
	protected void generateId() {
	}
}
