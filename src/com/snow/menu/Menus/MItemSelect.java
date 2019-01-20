package com.snow.menu.Menus;

import com.snow.menu.Buttons.Tools.BItemSelect;
import com.snow.menu.Buttons.Tools.Selector;
import com.snow.menu.MenuView;
import com.snow.menu.Menus.Attributes.NoBackMenu;
import com.snow.menu.Menus.Attributes.SelectorMenu;
import com.snow.menu.Util.NMSUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MItemSelect extends ListMenu<ItemStack> implements NoBackMenu, SelectorMenu {

	public static MItemSelect allItems;

	public MItemSelect() {
		super("Item Auswählen", NMSUtil.getAllItems(), item -> new BItemSelect(item));
	}

	/*public static MItemSelect initAll() {
		int slot = 9;
		MItemSelect head = new MItemSelect();
		List<MItemSelect> pages = new ArrayList<>();
		MItemSelect currentPage = head;
		pages.add(head);
		List<ItemStack> all = NMSUtil.getAllItems();

		for (ItemStack i : all) {
			//ItemStack i = new ItemStack(mat);
			if (i != null) {
				currentPage.addButton(new BItemSelect(i), slot);
				slot++;
				if (slot > 53) {
					slot = 9;
					currentPage = new MItemSelect();
					pages.add(currentPage);
				}
			}
		}
		PagedMenuHandler.init(pages.toArray(new MItemSelect[0]));
		allItems = head;
		return head;
	}*/

	// useSelector() Method needs to be called BEFORE calling this!
	// Instead, you can use show(Player, Selector)
	@Override
	public MenuView show(Player player) {
		if (BItemSelect.getSelector(player.getUniqueId()) == null) {
			throw new IllegalStateException("No Selector Specified!");
		}
		return super.show(player);
	}

	@Override
	public MenuView show(Player player, Selector selector) {
		BItemSelect.useSelector(player.getUniqueId(), selector);
		return show(player);
	}

	/*@Override
	public void onClosingMenu(Player player, MenuView view, MenuView target) {
		super.onClosingMenu(player, view, target);
		if (target == null || !(target.getMenu() instanceof MItemSelect)) {
			removeSelector(player.getUniqueId());
			PagedMenuHandler.clearCache(player);
		}
	}*/

}
