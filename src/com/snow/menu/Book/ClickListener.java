package com.snow.menu.Book;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClickListener implements CommandExecutor {

	private Map<String, BookButton> buttons = new HashMap<String, BookButton>();

	public void addButton(BookButton... button) {
		for (BookButton b : button) {
			buttons.put(b.getId(), b);
		}
	}

	public void removeAll(UUID player) {
		if (player == null) return;

		Iterator<Map.Entry<String, BookButton>> i = buttons.entrySet().iterator();
		while (i.hasNext()) {
			if (player.equals(i.next().getValue().getPlayer())) {
				i.remove();
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			Player player = ((Player) sender);
			if (args.length == 1) {
				if (!args[0].equals("NoCommand")) {
					BookButton button = buttons.get(args[0]);
					if (button != null && button.getPlayer().equals(player.getUniqueId())) {
						if (button.click(player)) {
							button.getText().displayTo(player);
							return true;
						}
					}
				}
			}
			removeAll(player.getUniqueId());
			//P.p.log("Removed all for player, now in map: " + buttons.size());
		}
		return true;
	}
}
