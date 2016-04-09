package com.snow.menu.Book;


import java.util.List;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftMetaBook;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import com.snow.mkremins.fanciful.FancyMessage;

import com.snow.menu.P;

public class BookText {

	BookMeta meta;

	public BookText() {
		if (meta == null) {
			meta = ((BookMeta) P.p.getServer().getItemFactory().getItemMeta(Material.WRITTEN_BOOK));
		}
		//meta.setAuthor("TextAuthor");
	}

	// Set the Text to something new, formatted with newline char
	public void setText(String text) {
		setPages(text);
	}


	// Set the Text to this FancyMessage
	// Does not split pages or format anything
	public void setText(FancyMessage fancyText) {
		Object cbc = fancyText.createChatBaseComponent();
		if (cbc != null && cbc instanceof IChatBaseComponent) {
			if (meta instanceof CraftMetaBook) {
				CraftMetaBook m = ((CraftMetaBook) meta);
				m.pages.clear();
				m.pages.add(((IChatBaseComponent) cbc));
			}
		}
	}

	// Set the Text on the specific page to this FancyMessage
	// Does not split pages or format anything
	public void setText(FancyMessage fancyText, int page) {
		if (meta instanceof CraftMetaBook) {
			Object cbc = fancyText.createChatBaseComponent();
			if (cbc != null && cbc instanceof IChatBaseComponent) {
				CraftMetaBook m = ((CraftMetaBook) meta);
				if (m.pages.size() < page) {
					throw new IndexOutOfBoundsException("page index larger than size: " + page + " > " + m.pages.size());
				}
				m.pages.set(page, ((IChatBaseComponent) cbc));
			}
		}
	}

	// Add some text to a new Page, formatted with newline char
	public void addText(String text) {
		if (!hasPages()) {
			setText(text);
		} else {
			addPage(text);
		}
	}

	// Add a new Page with this FancyMessage
	// Does not split pages or format anything
	public void addText(FancyMessage fancyText) {
		Object cbc = fancyText.createChatBaseComponent();
		if (cbc != null && cbc instanceof IChatBaseComponent) {
			if (meta instanceof CraftMetaBook) {
				((CraftMetaBook) meta).pages.add(((IChatBaseComponent) cbc));
			}
		}
	}


	// Add Some Text to a new Page, using an array for multiple lines
	// Will Split the Text to multiple pages if longer than 256 chars
	public void addLines(String... lines) {
		StringBuilder builder = new StringBuilder();
		for (String line : lines) {
			builder.append(line).append("\n");
		}
		addText(builder.toString());
	}

	// Set the Text to something new, using an array for multiple lines
	// Will Split the Text to multiple pages if longer than 256 chars
	public void setLines(String... lines) {
		StringBuilder builder = new StringBuilder();
		for (String line : lines) {
			builder.append(line).append("\n");
		}
		setText(builder.toString());
	}

	// The same with Lists
	public void addLines(List<String> list) {
		addLines(list.toArray(new String[list.size()]));
	}

	// The same with Lists
	public void setLines(List<String> list) {
		setLines(list.toArray(new String[list.size()]));
	}

	// Display this Text to a Player
	public void displayTo(final Player player) {
		player.closeInventory();
		final ItemStack book = new ItemStack(Material.WRITTEN_BOOK);
		book.setItemMeta(meta);
		final int slot = player.getInventory().getHeldItemSlot();
		final ItemStack replacing = player.getItemInHand();
		player.setItemInHand(book);
		player.updateInventory();

		((CraftPlayer) player).getHandle().openBook(new net.minecraft.server.v1_8_R3.ItemStack(net.minecraft.server.v1_8_R3.Items.WRITTEN_BOOK));

		player.getInventory().setItem(slot, replacing);
		player.updateInventory();

		/*P.p.getServer().getScheduler().scheduleSyncDelayedTask(P.p, new Runnable() {
			@Override
			public void run() {
				if (player.isOnline()) {
					CraftPlayer pl = (CraftPlayer) player;
					pl.getHandle().openBook(CraftItemStack.asNMSCopy(book));
					player.getInventory().setItem(slot, replacing);
				}
			}
		}, 5);*/

		/*P.p.getServer().getScheduler().scheduleSyncDelayedTask(P.p, new Runnable() {
			@Override
			public void run() {
				failSafeRemove(player, slot, replacing);
			}
		}, 5);

		P.p.getServer().getScheduler().scheduleSyncDelayedTask(P.p, new Runnable() {
			@Override
			public void run() {
				failSafeRemove(player, slot, replacing);
			}
		}, 10);*/

	}

	/*private void failSafeRemove(Player player, int slot, ItemStack replacing) {
		if (player.isOnline()) {
			ItemStack item = player.getInventory().getItem(slot);
			if (item != null && item.getType().equals(Material.WRITTEN_BOOK)) {
				if (item.getItemMeta() instanceof BookMeta) {
					if (((BookMeta) item.getItemMeta()).getAuthor().equals("TextAuthor")) {
						player.getInventory().setItem(slot, replacing);
					}
				}
			}
		}
	}*/

	// ---  Some more Book Methods  ---

	public void addPage(String... pages) {
		meta.addPage(pages);
	}

	public int getPageCount() {
		return meta.getPageCount();
	}

	public String getPage(int page) {
		return meta.getPage(page);
	}

	public boolean hasPages() {
		return meta.hasPages();
	}

	public void setPage(int i, String s) {
		meta.setPage(i, s);
	}

	public List<String> getPages() {
		return meta.getPages();
	}

	public void setPages(List<String> list) {
		meta.setPages(list);
	}

	public void setPages(String... strings) {
		meta.setPages(strings);
	}


	// ---  Static helper methods  ---

	// Split the Text into multiple pages, 256 chars per page
	public static String[] splitText(String text) {
		return splitText(text, 256);
	}

	// Split the Text into multiple pages, given amount of chars per page
	public static String[] splitText(String text, int chars) {
		if (text.length() <= chars) {
			return new String[] {text};
		}
		String pages[] = new String[(int) Math.ceil(text.length() / chars)];
		for (int i = 0; i < pages.length; i++) {
			int from = 256 * i;
			if (from > text.length()) {
				break;
			}
			int to = from + 256;
			if (to > text.length()) {
				to = text.length();
			}
			pages[i] = text.substring(from, to);
		}
		return pages;
	}
}
