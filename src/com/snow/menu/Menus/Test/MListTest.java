package com.snow.menu.Menus.Test;

import com.snow.menu.Buttons.Button;
import com.snow.menu.MenuView;
import com.snow.menu.Menus.ListMenu;
import com.snow.menu.P;
import com.snow.menu.Util.NMSUtil;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

public class MListTest extends ListMenu<ItemStack> {
	Iterator<ItemStack> iter;
	private int task = -1;


	public MListTest() {
		super("List", new ArrayList<>(), i -> new Button(i));
		//iter = NMSUtil.getAllItems().iterator();
		//setAutoUpdate(true);
		//setUpdateInterval(20);
		//addNonListButton(iter.next());
		//addNonListButton(iter.next());
	}

	public MListTest(String name, boolean init) {
		super(name, init);
	}


	@Override
	public MListTest creatingNewMenuPage() {
		return new MListTest(getName(), false);
	}

	@Override
	public void onClickOnEmptySlot(InventoryClickEvent event, MenuView view) {
		/*MListTest menu = (MListTest) getMenuPages().getFirst().getMenu();
		if (menu != this) {
			menu.onClickOnEmptySlot(event, view);
			return;
		}
		if (task != -1) {
			P.p.getServer().getScheduler().cancelTask(task);
			task = -1;
			return;
		}
		task = P.p.getServer().getScheduler().scheduleSyncRepeatingTask(P.p, () -> {
			if (iter.hasNext()) {
				addNonListButton(iter.next());
				P.p.log("Adding Button");
			}
		}, 10, 10);*/
	}
}
