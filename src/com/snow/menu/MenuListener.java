package com.snow.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.snow.menu.Buttons.TopList.TopList;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.snow.menu.Buttons.Tools.BItemSelect;
import com.snow.menu.Buttons.Tools.BMenuBook;
import com.snow.menu.Menus.Attributes.EditableMenuHandler;

public class MenuListener implements Listener {

	public static Map<UUID, MenuView> closing = new HashMap<UUID, MenuView>();

	public static void storeClosing(final UUID uuid, MenuView view) {
		closing.put(uuid, view);
		P.p.getServer().getScheduler().runTaskLater(P.p, new Runnable() {
			@Override
			public void run() {
				closing.remove(uuid);
			}
		} ,2);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onClick(InventoryClickEvent event) {
		// Cancel the event immediately in case anything happens
		event.setCancelled(true);

		// Check if menu book was clicked
		if (event.getClick().equals(ClickType.RIGHT)) {
			if (event.getCurrentItem() != null) {
				if (event.getWhoClicked() != null && event.getWhoClicked() instanceof Player) {
					if (BMenuBook.isMenuBook(event.getCurrentItem())) {
						P.p.log(event.getWhoClicked().getName() + " is opening the Main Menu");
						P.p.getPluginRegistry().getMainMenu().show((Player) event.getWhoClicked());
						return;
					}
				}
			}
		}

		// If there is no top inventory, it cant be the menu
		if (event.getView().getTopInventory() == null) {
			event.setCancelled(false);
			return;
		}

		// The holder would be the MenuView
		InventoryHolder holder = event.getView().getTopInventory().getHolder();
		if (holder == null || !(holder instanceof MenuView)) {
			event.setCancelled(false);
			return;
		}

		// Check if the player clicked
		if (event.getWhoClicked() == null || !(event.getWhoClicked() instanceof Player)) {
			return;
		}

		// Get the Menuview and check what was clicked
		MenuView view = (MenuView) holder;
		if (event.getClickedInventory() == null || event.getClickedInventory().getHolder() != holder) {
			view.getMenu().onClickOnPlayerInv(event, view);
			return;
		}
		if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) {
			view.getMenu().onClickOnEmptySlot(event, view);
			return;
		}
		view.getMenu().clickOnSlot(event, view);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.LOW)
	public void onDrag(InventoryDragEvent event) {
		// Cancel the event immediately in case anything happens
		event.setCancelled(true);

		// If there is no top inventory, it cant be the menu
		if (event.getView().getTopInventory() == null) {
			event.setCancelled(false);
			return;
		}

		// The holder would be the MenuView
		InventoryHolder holder = event.getView().getTopInventory().getHolder();
		if (holder == null || !(holder instanceof MenuView)) {
			event.setCancelled(false);
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onClose(InventoryCloseEvent event) {
		Inventory inv = event.getInventory();
		if (inv.getHolder() != null && inv.getHolder() instanceof MenuView) {
			MenuView close = ((MenuView) inv.getHolder());
			MenuView closed = closing.get(event.getPlayer().getUniqueId());
			if (closed != null && close == closed) {
				closing.remove(event.getPlayer().getUniqueId());
				return;
			}
			if (event.getPlayer() instanceof Player) {
				close.getMenu().onClosingMenu(((Player) event.getPlayer()), close, null);
			}
			close.closed();
		}
	}

	@EventHandler(ignoreCancelled = false)
	public void onInteract(PlayerInteractEvent event) {
		if (event.hasItem() && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
			if (BMenuBook.isMenuBook(event.getItem())) {
				event.setCancelled(true);
				P.p.log(event.getPlayer().getName() + " is opening the Main Menu");
				P.p.getPluginRegistry().getMainMenu().show(event.getPlayer());
			}
		}


		/*if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
			if (event.getClickedBlock().getType().equals(Material.FENCE)) {
				if (e != null) {
					NMSUtil.leash(e, event.getPlayer().getWorld().spawnEntity(event.getClickedBlock().getLocation(), EntityType.LEASH_HITCH), event.getPlayer());
					P.p.log("leashdone");
					e = null;
					return;
				}
				e = event.getPlayer().getWorld().spawnEntity(event.getClickedBlock().getLocation(), EntityType.LEASH_HITCH);
				P.p.log("leash");
			}
		}*/
	}

	/*@EventHandler
	public void onE(PlayerInteractEntityEvent event) {
		if (event.getRightClicked().getType() == EntityType.LEASH_HITCH || event.getRightClicked().getType() == EntityType.ARMOR_STAND) {
			if (e != null) {
				NMSUtil.leash(e, event.getRightClicked(), event.getPlayer());
				P.p.log("leashdone");
				e = null;
				return;
			}
			e = event.getRightClicked();
			P.p.log("leash");
		}
	}*/

	@EventHandler
	public void onLeave(PlayerQuitEvent event) {
		ClickSpam.remove(event.getPlayer().getUniqueId());
		EditableMenuHandler.clear(event.getPlayer().getUniqueId());
		BItemSelect.removeSelector(event.getPlayer().getUniqueId());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
	public void onKick(PlayerKickEvent event) {
		ClickSpam.remove(event.getPlayer().getUniqueId());
		EditableMenuHandler.clear(event.getPlayer().getUniqueId());
		BItemSelect.removeSelector(event.getPlayer().getUniqueId());
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onJoin(PlayerJoinEvent event) {
		TopList.updateNames(event.getPlayer().getUniqueId(), event.getPlayer().getName());
	}

}
