package com.snow.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.snow.menu.Buttons.Button;

/*
 * stores the last clicks of each player, to check if somebody spams clicks
 * Click-times are only removed on player leave
 */

public class ClickSpam {
	public static Map<UUID, ClickSpam> map = new HashMap<UUID, ClickSpam>();
	public Button button;
	public long time;

	public ClickSpam(Button button, long time) {
		this.button = button;
		this.time = time;
	}

	public static boolean canClick(Button b, Player player, MenuView view) {
		ClickSpam spam = map.get(player.getUniqueId());
		if (spam == null) {
			map.put(player.getUniqueId(), new ClickSpam(b, System.currentTimeMillis()));
			return true;
		}
		return spam.click(b, player, view);
	}

	public boolean click(Button b, Player player, MenuView view) {
		int delay = b.getClickDelay(player, view, b == button);
		boolean can = (delay <= 0 || System.currentTimeMillis() - time > delay);
		time = System.currentTimeMillis();
		button = b;
		return can;
	}

	public static void remove(UUID uuid) {
		map.remove(uuid);
	}
}
