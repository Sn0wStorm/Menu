package com.snow.menu.Book;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import com.snow.menu.P;

public class BookListener implements Listener {

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (ItemReplacer.isEmpty()) {
			return;
		}
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.hasItem()) {
				ItemStack item = event.getItem();
				if (item != null && item.getType().equals(Material.BOOK_AND_QUILL)) {
					ItemReplacer replacer = ItemReplacer.get(event.getPlayer().getName());
					if (replacer != null) {
						replacer.hasOpened = true;
						return;
					}
				}
			}
		}
		illegalAction(event.getPlayer());
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		illegalAction(event.getPlayer());
		BookButton.clickListener.removeAll(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onKick(PlayerKickEvent event) {
		illegalAction(event.getPlayer());
		BookButton.clickListener.removeAll(event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		illegalAction(event.getPlayer());
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		if (ItemReplacer.isEmpty()) {
			return;
		}
		Location from = event.getFrom();
		Location to = event.getTo();
		if (Math.abs(from.getYaw() - to.getYaw()) > 2 || Math.abs(from.getPitch() - to.getPitch()) > 2) {
			illegalAction(event.getPlayer());
		}
	}

	@EventHandler
	public void onOpenInv(InventoryOpenEvent event) {
		if (event.getPlayer() instanceof Player) {
			illegalAction((Player) event.getPlayer());
			BookButton.clickListener.removeAll(event.getPlayer().getUniqueId());
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onInvClick(InventoryClickEvent event) {
		if (event.getWhoClicked() instanceof Player) {
			illegalAction((Player) event.getWhoClicked());
		}
	}

	@EventHandler
	public void onSlotChange(PlayerItemHeldEvent event) {
		illegalAction(event.getPlayer());
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		illegalAction(event.getEntity());
	}

	@EventHandler(ignoreCancelled = true)
	public void onTeleport(PlayerTeleportEvent event) {
		illegalAction(event.getPlayer());
	}

	// The player may not have opened the book for editing... Remove it
	public void illegalAction(Player player) {
		if (ItemReplacer.isEmpty()) {
			return;
		}
		ItemReplacer replacer = ItemReplacer.get(player.getName());
		if (replacer != null) {
			boolean hadOpen = replacer.hasOpened;
			replacer.revert(player, true);
			EditableBookText book = replacer.getBook();
			if (book != null) {
				book.wasRemoved(player, hadOpen);
			}
		}
	}


	@EventHandler(ignoreCancelled = true)
	public void onBookEdit(PlayerEditBookEvent event) {
		P.p.log("edited book");
		if (ItemReplacer.isEmpty()) {
			return;
		}
		Player player = event.getPlayer();
		ItemReplacer replacer = ItemReplacer.get(player.getName());
		if (replacer != null) {
			event.setCancelled(true);
			replacer.revert(player, true);
			EditableBookText book = replacer.getBook();
			if (book != null) {
				List<String> pages = new ArrayList<String>();
				BookMeta meta = event.getNewBookMeta();
				if (meta != null && meta.getPageCount() > 0) {
					for (String old : meta.getPages()) {
						pages.add(old.replaceAll("§0", ""));
					}
				}
				book.meta = meta;
				book.onEdit(event, pages);
			}
		}
	}
}
