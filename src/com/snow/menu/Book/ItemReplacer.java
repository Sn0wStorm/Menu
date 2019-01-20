package com.snow.menu.Book;

import com.snow.menu.P;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class ItemReplacer {

	// Used by the EditableBookText
	// Puts an item into the hand of a player and later reverts it back to the one there was before

	private static Map<String, ItemReplacer> replacedItems = new HashMap<>(); // Player Name and replacer

	private ItemStack replaced;
	private EditableBookText book;
	public boolean hasOpened;
	//private BukkitTask warn;
	private BukkitTask timeout;


	public ItemReplacer(ItemStack replaceWith, Player player, EditableBookText book) {
		this.book = book;
		if (replacedItems.containsKey(player.getName())) {
			revertPlayer(player);
		}
		replacedItems.put(player.getName(), this);
		replace(player, replaceWith);
		initTimeout(player);
	}

	private void replace(Player player, ItemStack replaceWith) {
		replaced = player.getInventory().getItemInMainHand();
		player.getInventory().setItemInMainHand(replaceWith);
		player.updateInventory();
	}

	private void initTimeout(final Player player) {
		/*warn = P.p.getServer().getScheduler().runTaskLater(P.p, new Runnable() {
			@Override
			public void run() {
				// send a title
			}
		}, 30)*/

		timeout = P.p.getServer().getScheduler().runTaskLater(P.p, () -> {
			if (!hasOpened) {
				revert(player, true);
				if (book != null) {
					book.wasRemoved(player, false);
				}
			}
		}, 80);
	}

	// Revert the Item in the players hand to the one it was before, optional remove from list
	public void revert(Player player, boolean remove) {
		hasOpened = true;
		timeout.cancel();
		if (player.isOnline()) {
			player.getInventory().setItemInMainHand(replaced);

			player.updateInventory();

			P.p.getServer().getScheduler().scheduleSyncDelayedTask(P.p, () -> {
				player.getInventory().setItemInMainHand(replaced);
				player.updateInventory();
			});
		}
		if (remove) {
			replacedItems.remove(player.getName());
		}
	}

	// Gets the book that has made this replace
	public EditableBookText getBook() {
		return book;
	}

	// Get a replacer from the list, null if not found
	public static ItemReplacer get(String player) {
		return replacedItems.get(player);
	}

	// Revert the Item in the players hand to the one it was before, returns false if player was not in the list
	public static boolean revertPlayer(Player player) {
		ItemReplacer replacer = replacedItems.get(player.getName());
		if (replacer != null) {
			replacer.revert(player, true);
			return true;
		}
		return false;
	}

	public static void revertAll() {
		for (Map.Entry<String, ItemReplacer> entry : replacedItems.entrySet()) {
			Player player = P.p.getServer().getPlayerExact(entry.getKey());
			if (player != null) {
				entry.getValue().revert(player, false);
			}
		}
		replacedItems.clear();
	}

	public static boolean isEmpty() {
		return replacedItems.isEmpty();
	}

}
