package com.snow.menu.Menus.Attributes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.snow.menu.Buttons.Attributes.ImmovableButton;
import com.snow.menu.Buttons.Attributes.TakeableButton;
import com.snow.menu.Buttons.Basic.BEditMenu;
import com.snow.menu.Buttons.Button;
import com.snow.menu.Buttons.Common.BPlayerAdded;
import com.snow.menu.Buttons.GlobalEditableButton;
import com.snow.menu.Buttons.Tools.Selector;
import com.snow.menu.MenuView;
import com.snow.menu.Menus.MItemSelect;
import com.snow.menu.P;

public class EditableMenuHandler implements Selector {

	protected EditableMenu menu;
	protected boolean realButtons;
	protected boolean showSelector;
	protected static Map<UUID, Button> moving = new HashMap<UUID, Button>();
	protected static Map<UUID, Integer> placing = new HashMap<UUID, Integer>();

	public EditableMenuHandler(EditableMenu menu) {
		this.menu = menu;
		realButtons = false;
	}

	// If realbuttons is true, (default false) Buttons can be added and later removed like normal ItemStacks
	//   This means adding an item will not copy it into the menu, but rather place it in
	//   Items a Player placed into the Menu can later be taken out by a Player and put into his Inventory
	//   Buttons that implement TakeableButton can also be taken out by a Player and put into his Inventory
	// If showSelector is true, a Menu with every Minecraft Item is opened when clicking an empty slot
	//   Clicking an item will copy it into the clicked empty slot
	public EditableMenuHandler(EditableMenu menu, boolean realButtons, boolean showSelector) {
		this.menu = menu;
		this.realButtons = realButtons;
		this.showSelector = showSelector;
	}


	// Either call these 4 methods in the respective Menu methods
	// to use default editing of buttons
	// OR use the other methods below to manually specify editing

	// When Clicking on a Button, handles taking buttons out of the Menu
	public void clickOnSlot(InventoryClickEvent event, MenuView view) {
		if (!menu.canEdit(((Player) event.getWhoClicked()))) return;
		if (!event.getClick().equals(ClickType.LEFT) && !event.getClick().equals(ClickType.SHIFT_RIGHT) && !event.getClick().equals(ClickType.SHIFT_LEFT)) return;
		if (event.getCursor() == null || event.getCursor().getType().equals(Material.AIR)) {
			if (event.getClick().equals(ClickType.LEFT) && event.getAction().equals(InventoryAction.PICKUP_ALL)) {
				playerTakeItemOutMenu(event, view);
			} else if ((realButtons && event.getClick().equals(ClickType.SHIFT_LEFT)) || event.getClick().equals(ClickType.SHIFT_RIGHT)) {
				if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
					// Keep this cancelled if realButtons is false!
					playerRemoveButton(event, view);
				}
			}
		}
	}

	// When clicking an empty Slot in the menu, handles placing items into the Menu
	public void clickOnEmptySlot(InventoryClickEvent event, MenuView view) {
		if (!menu.canEdit(((Player) event.getWhoClicked()))) return;
		if (event.getClick().equals(ClickType.LEFT) && event.getAction().equals(InventoryAction.PLACE_ALL)) {
			playerPutItemInMenu(event, view);
		} else if (showSelector && (event.getCursor() == null || event.getCursor().getType().equals(Material.AIR))) {
			if (event.getAction().equals(InventoryAction.NOTHING)) {
				placing.put(event.getWhoClicked().getUniqueId(), event.getSlot());
				MItemSelect.allItems.show(((Player) event.getWhoClicked()), this);
			}
		}
	}

	// When clicking outside of the Menu, handles taking and returning items
	// that the player took out of their inv but didnt place into the menu
	public void clickOnPlayerInv(final InventoryClickEvent event, MenuView view) {
		if (!menu.canEdit(((Player) event.getWhoClicked()))) return;
		if (moving.containsKey(event.getWhoClicked().getUniqueId())) {
			if (!realButtons) {
				return;
			} else {
				switch (event.getAction()) {
					case PLACE_ALL:
					case SWAP_WITH_CURSOR:
						Button take = moving.get(event.getWhoClicked().getUniqueId());
						if (take instanceof TakeableButton) {
							moving.remove(event.getWhoClicked().getUniqueId());
							event.setCancelled(false);
							final ItemStack item = ((TakeableButton) take).takeoutItem();
							if (item != null) {
								P.p.getServer().getScheduler().scheduleSyncDelayedTask(P.p, new Runnable() {
									@Override
									public void run() {
										event.getClickedInventory().setItem(event.getSlot(), item);
									}
								}, 1);
							}
						}
					default:
				}
			}
			return;
		}
		/*Button moved = moving.get(event.getWhoClicked().getUniqueId());
		if (moved != null && !(moved instanceof BPlayerAdded)) {
			return;
		}*/
		switch (event.getAction()) {
			case PICKUP_ALL:
			case PICKUP_HALF:
			case PICKUP_ONE:
			case PICKUP_SOME:
			case PLACE_ALL:
			case PLACE_ONE:
			case PLACE_SOME:
			case SWAP_WITH_CURSOR:
				//moving.remove(event.getWhoClicked().getUniqueId());
				event.setCancelled(false);
				return;
			case MOVE_TO_OTHER_INVENTORY:
				if (realButtons && (event.getClick().equals(ClickType.SHIFT_LEFT) || event.getClick().equals(ClickType.SHIFT_RIGHT))) {
					int slot = view.getInventory().firstEmpty();
					if (slot >= 0) {
						if (slot < view.getMenu().getButtons().length) {
							if (view.getMenu().getButton(slot) == null) {
								playerPlacedNewButton(event.getCurrentItem(), slot, event, view);
							}
						}
					}
				}
			default:
		}
	}

	// When closing the Menu, puts a Button from the cursor back into the menu
	public void closingMenu(Player player, MenuView view) {
		if (moving.containsKey(player.getUniqueId())) {
			player.setItemOnCursor(new ItemStack(Material.AIR));
			cancelMoving(player.getUniqueId());
		}
	}


	// Called by a BItemSelect Button when it is clicked and this was registered as selector
	@Override
	public void selected(UUID player, Button button) {
		if (!placing.containsKey(player)) return;
		int slot = placing.get(player);
		placing.remove(player);
		playerCreatedNewButton(button.getItem().clone(), slot, player);
	}

	// Player just placed an Item into a Menu
	public void playerPutItemInMenu(InventoryClickEvent event, MenuView view) {
		if (moving.containsKey(event.getWhoClicked().getUniqueId())) {
			playerMovedButton(event, view);
		} else if (event.getCursor() != null && !event.getCursor().getType().equals(Material.AIR)) {
			if (realButtons) {
				playerPlacedNewButton(event.getCursor(), event.getSlot(), event, view);
			} else {
				playerCreatedNewButton(event.getCursor().clone(), event.getSlot(), event.getWhoClicked().getUniqueId());
			}
		}
	}

	// Player just took an Item out of a Menu
	public void playerTakeItemOutMenu(InventoryClickEvent event, MenuView view) {
		Button button = menu.getButton(event.getSlot());
		if (button == null) return;
		if (button instanceof BEditMenu) {
			if (button.canClick(((Player) event.getWhoClicked()), view)) {
				button.click(event, view);
			} else {
				button.clickNotAllowed(event, view);
			}
			return;
		}
		if (button instanceof ImmovableButton || !button.isMovable()) return;

		moving.put(event.getWhoClicked().getUniqueId(), button);
		menu.removeButton(event.getSlot());
		event.setCancelled(false);
	}

	// Player removes a Button
	public void playerRemoveButton(InventoryClickEvent event, MenuView view) {
		Button button = menu.getButton(event.getSlot());
		if (button == null) return;
		if (button instanceof ImmovableButton || !button.isMovable()) return;
		if (realButtons) {
			if (button instanceof TakeableButton) {
				if (event.getWhoClicked().getInventory().firstEmpty() < 0) return;
				ItemStack item = ((TakeableButton) button).takeoutItem();
				if (item == null) {
					menu.removeButton(event.getSlot());
					event.setCancelled(false);
				} else {
					menu.removeButton(event.getSlot());
					event.getWhoClicked().getInventory().addItem(item);
					menu.updateSlot(event.getSlot());
				}
			}
		} else {
			menu.removeButton(event.getSlot());
			menu.updateSlot(event.getSlot());
		}
	}

	// The Item the player placed into the Menu was already a Button
	public void playerMovedButton(InventoryClickEvent event, MenuView view) {
		int slot = event.getSlot();
		if (menu.getRows() * 9 > slot) {
			if (menu.getButtons()[slot] == null) {
				event.setCancelled(false);
				Button button = moving.get(event.getWhoClicked().getUniqueId());
				menu.addButton(button, slot);
				moving.remove(event.getWhoClicked().getUniqueId());
			}
		}
	}

	// The Player created a new Button in the Menu (by selecting or copying an item)
	public void playerCreatedNewButton(ItemStack item, int slot, UUID player) {
		//if (event.getWhoClicked().isOp()) {
		menu.addButton(new GlobalEditableButton(item).init(true), slot);
		menu.updateSlot(slot);
		//} else {
			//menu.addButton(new BPlayerAdded(event.getCursor().clone()), event.getSlot());
		//}
		//event.setCancelled(false);
	}

	// The Player placed an Item into the Menu (only ever called if realButtons is true)
	// The InventoryClickEvent does not necessarily have the correct slot and item, use the given ones
	public void playerPlacedNewButton(ItemStack item, int slot, InventoryClickEvent event, MenuView view) {
		menu.addButton(new BPlayerAdded(item), slot);
		event.setCancelled(false);
	}

	public EditableMenu getMenu() {
		return menu;
	}

	// When a player cancels moving (like closing the inventory)
	public void cancelMoving(UUID uuid) {
		Button button = moving.get(uuid);
		if (button != null) {
			for (int i = 0; i < menu.getRows() * 9; i++) {
				if (menu.getButtons()[i] == null) {
					menu.addButton(button, i);
					break;
				}
			}
		}
		moving.remove(uuid);
	}

	// ONLY call for Player leaving etc.
	public static void clear(final UUID uuid) {
		P.p.getServer().getScheduler().scheduleSyncDelayedTask(P.p, new Runnable() {
			@Override
			public void run() {
				moving.remove(uuid);
				placing.remove(uuid);
			}
		}, 2);
	}
}
