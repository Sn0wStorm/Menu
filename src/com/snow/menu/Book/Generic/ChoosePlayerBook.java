package com.snow.menu.Book.Generic;

import com.snow.menu.Book.EditableBookText;
import org.bukkit.event.player.PlayerEditBookEvent;

import java.util.List;

public abstract class ChoosePlayerBook extends EditableBookText {

	public ChoosePlayerBook() {
		super();
		addLines("Gib den Namen des Spielers ein: ", "");
	}

	@Override
	public void onEdit(PlayerEditBookEvent event, List<String> pages) {
		if (pages.isEmpty()) return;
		String page = pages.get(pages.size() - 1);
		String[] split = page.split(" ");
		String name = split[split.length - 1];
		name = name.replaceAll("\n", "");
		if (name.length() > 16 || name.length() < 2) {
			event.getPlayer().sendMessage("§cKein gültiger Name eingegeben");
			return;
		}
		onNameEntered(event, pages, name);
	}

	protected abstract void onNameEntered(PlayerEditBookEvent event, List<String> pages, String name);
}
