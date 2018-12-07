package com.snow.menu.Buttons.TopList;

import com.snow.menu.Book.BookText;
import com.snow.menu.Buttons.Button;
import com.snow.menu.MenuView;
import com.snow.mkremins.fanciful.FancyMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class TopButton extends Button {

	protected final TopList topList;

	public TopButton(TopList topList) {
		super(Material.GOLD_BLOCK, "Topliste", "Liste der Top 100 Spieler");
		this.topList = topList;
	}

	public TopButton(Material type, TopList topList) {
		super(type);
		this.topList = topList;
	}

	public TopButton(Material type, TopList topList, short durability) {
		super(type, durability);
		this.topList = topList;
	}

	public TopButton(Material type, TopList topList, String name) {
		super(type, name);
		this.topList = topList;
	}

	public TopButton(Material type, TopList topList, short durability, String name) {
		super(type, durability, name);
		this.topList = topList;
	}

	public TopButton(Material type, TopList topList, String name, String... lore) {
		super(type, name, lore);
		this.topList = topList;
	}

	public TopButton(Material type, TopList topList, short durability, String name, String... lore) {
		super(type, durability, name, lore);
		this.topList = topList;
	}

	public TopButton(ItemStack item, TopList topList) {
		super(item);
		this.topList = topList;
	}

	// Called when Showing Button to the Player
	// Should be overridden to get the Players level more performant
	// Also with only this it only works if the player is in the top 100
	protected int getLevel(Player player) {
		return topList.getPlayerLevel(player.getUniqueId());
	}

	@Override
	public ItemStack getItem(Player player, MenuView view) {
		update(player);
		return super.getItem(player, view);
	}

	protected void update(Player player) {
		setLoreLine(2, "§aDeine Position: §3" + topList.getRank(getLevel(player), player.getUniqueId()));
	}

	@Override
	public void click(InventoryClickEvent event, MenuView view) {
		topList.open(((Player) event.getWhoClicked()));
	}

}
