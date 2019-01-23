package com.snow.menu.Book;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class BookListener implements Listener {



	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (ItemReplacer.isEmpty()) {
			return;
		}
		if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			if (event.hasItem()) {
				ItemStack item = event.getItem();
				if (item != null && item.getType().equals(Material.WRITABLE_BOOK)) {
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
		if (Math.abs(from.getYaw() - to.getYaw()) > 4 || Math.abs(from.getPitch() - to.getPitch()) > 4) {
			illegalAction(event.getPlayer());
		}
	}

	// Disallow crafting with the MenuBook and the Book that is used by the EditableBookText in case someone can get it
	@EventHandler
	public void onPrepareCraft(PrepareItemCraftEvent event) {
		Recipe recipe = event.getRecipe();
		if (recipe == null) return;
		if (recipe.getResult().getType() == Material.BOOKSHELF) {
			for (ItemStack i : event.getInventory().getMatrix()) {
				if (i != null && i.getType() == Material.BOOK && i.hasItemMeta()) {
					ItemMeta itemMeta = i.getItemMeta();
					if (itemMeta.hasDisplayName()) {
						String displayName = itemMeta.getDisplayName();
						if (displayName.equals("§fRechtsklick") || displayName.equals("Rechtsklick") || displayName.equals("§7Menü Buch") || displayName.equals("Menü Buch")) {
							event.getInventory().setResult(new ItemStack(Material.AIR));
							return;
						}
					}
				}
			}
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

	@EventHandler(ignoreCancelled = true)
	public void onItemSpawn(ItemSpawnEvent event) {
		ItemStack item = event.getEntity().getItemStack();
		event.setCancelled(item.getType() == Material.WRITABLE_BOOK
				&& item.hasItemMeta()
				&& item.getItemMeta().hasDisplayName()
				&& item.getItemMeta().getDisplayName().equals("§fRechtsklick"));
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
		//P.p.log("edited book");
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
				List<String> pages = new ArrayList<>();
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
