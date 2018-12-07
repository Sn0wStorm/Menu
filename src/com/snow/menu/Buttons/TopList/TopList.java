package com.snow.menu.Buttons.TopList;

import com.snow.menu.Book.BookText;
import com.snow.menu.P;
import com.snow.mkremins.fanciful.FancyMessage;
import org.apache.commons.lang.mutable.MutableInt;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;


public class TopList {

	public static Set<TopList> registeredTopLists = new HashSet<TopList>();

	public static final int UNSIGNED_SHORT_LENGTH = Short.MAX_VALUE * 2;
	public static final String LISTFILE = "TopList";
	public static final String LEVELSFILE = "TopLevels";
	private static final String DEFHEADER = "§8§lTopliste\n\n";

	private final File topFolder;

	private TreeSet<TopPlayer> list = new TreeSet<TopPlayer>();
	private ArrayList<MutableInt> levels;
	protected BookText text;

	private int lastLevelInList = 1;
	private long timeGenerated;
	private boolean changed = true;


	// If topFolder == null it will not save/load from file
	public TopList(File topFolder) {
		this.topFolder = topFolder;
		try {
			loadFromFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static TopList registerTopList(TopList tl) {
		registeredTopLists.add(tl);
		return tl;
	}

	public static TopList unregisterTopList(TopList tl) {
		registeredTopLists.remove(tl);
		return tl;
	}

	public void putLevel(long time, int level, String player, UUID uuid) {
		if (list.size() >= 100 && level < lastLevelInList) {
			updateLevels(level);
			saveToFile(false);
		} else {

			Iterator<TopPlayer> iter;
			if (level > lastLevelInList + 55) {
				iter = list.descendingIterator();
			} else {
				iter = list.iterator();
			}
			changed = true;
			while (iter.hasNext()) {
				TopPlayer existing = iter.next();
				if (existing.plId.equals(uuid)) {
					iter.remove();
					existing.level = level;
					existing.time = time;
					list.add(existing);
					saveToFile(true);
					return;
				}
			}
			if (list.size() >= 100) {
				list.pollFirst();
			}
			list.add(new TopPlayer(level, time, player, uuid));
			lastLevelInList = list.first().level;

			saveToFile(true);
		}
	}

	public void open(Player player) {
		if (wasChanged()) {
			updateBook();
			setChanged(false);
		}

		text.displayTo(player);
	}

	protected void updateBook() {
		text = new BookText();
		//msg.then(QuestTop.getRank(qp.getCombinedQuestLevel(), qp.getId()) + "\n\n").color(ChatColor.DARK_AQUA);
		/*msg.then("3475\n").color(ChatColor.DARK_GRAY);
		msg.then("1 ").color(ChatColor.DARK_GRAY);
		msg.then("Sn0wSt0rm\n").color(ChatColor.BLACK);
		msg.then("2 ").color(ChatColor.DARK_GRAY);
		msg.then("SomeOther\n").color(ChatColor.BLACK);*/
		boolean first = true;
		String prefix = getLevelPrefix();
		String postfix = getLevelPostfix();
		for (FancyMessage page : getFancyPages(11, 13, prefix, postfix)) {
			if (first) {
				first = false;
				FancyMessage f = new FancyMessage(getHeader());
				f.then(page);
				text.addText(f);
			} else {
				text.addText(page);
			}
		}
	}

	public TopPlayer getFirstPlayer() {
		return list.last();
	}

	public String getLevelPrefix() {
		return "";
	}

	public String getLevelPostfix() {
		return "Level";
	}

	// Can be overridden to show a different Header on the Book
	public String getHeader() {
		return DEFHEADER;
	}

	public boolean wasChanged() {
		return changed;
	}

	// Set to true if a change was made and the Book needs to be rebuilt
	// Happens automatically with call of putLevel
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	private void changeLevel(int from, int to) {
		from--;
		to--;
		if (from < to) {
			if (levels.size() <= to) {
				if (levels.size() < to - 4) {
					levels.ensureCapacity(to + 32);
				}
				while (levels.size() < from) {
					levels.add(new MutableInt(102));
				}
				while (levels.size() > from) {
					levels.get(from).increment();
					from++;
				}
				while (from < to) {
					levels.add(new MutableInt(102));
					from++;
				}
				levels.add(new MutableInt(101));
			} else {
				while (from < to) {
					levels.get(from).increment();
					from++;
				}
			}
		} else if (from > to) {
			if (levels.size() <= to) {
				changeLevel(levels.size(), to);
			} else {
				while (from >= to) {
					levels.get(from).decrement();
					from--;
				}
			}
		}
	}

	private void updateLevels(int level) {
		level--;
		if (levels.size() <= level) {
			if (levels.size() < level - 4) {
				levels.ensureCapacity(level + 32);
			}
			/*if (!levels.isEmpty()) {
				prev = levels.get(levels.size() - 1).intValue();
			}*/
			while (levels.size() <= level) {
				levels.add(new MutableInt(101));
			}
			//levels.add(new MutableInt(1));
		}
		/*if (levels.size() >= done) {
			levels.add(new MutableInt(1));
		}*/
		/*for (int i = done - 1; i >= 0; i--) {
			levels.get(i).increment();
		}*/
		if (level > 0) {
			levels.get(level - 1).increment();
		}
	}

	// Gets the Players level in this toplist, only if he is in the Top 100
	public int getPlayerLevel(UUID id) {
		for (TopPlayer tp : list) {
			if (tp.plId.equals(id)) {
				return tp.level;
			}
		}
		return 0;
	}

	public int getRank(int level, UUID id) {
		if (level <= 0) {
			return 0;
		}
		if (level >= lastLevelInList) {
			Iterator<TopPlayer> iter;
			int rank;
			if (level > lastLevelInList + 55) {
				iter = list.descendingIterator();
				rank = 1;
			} else {
				rank = -list.size();
				iter = list.iterator();
			}
			while (iter.hasNext()) {
				TopPlayer t = iter.next();
				if (t.level == level && t.plId.equals(id)) {
					return Math.abs(rank);
				}
				rank++;
			}
			return 0;
		}
		level--;
		if (level >= levels.size()) {
			return 0;
		}
		return levels.get(level).intValue();
	}

	public void check(int level, long time, String player, UUID id) {
		if (time <= 0 || level <= 1) return;
		if (time < timeGenerated) {
			if (list.size() >= 100) {
				if (level < lastLevelInList) {
					changeLevel(1, level);
				} else {
					changeLevel(1, levels.size() - 1);
				}
			}
		}
	}

	public List<String> getStringList(int firstLength, int pageLength) {
		if (list.isEmpty()) {
			List<String> out = new ArrayList<String>(1);
			out.add("");
			return out;
		}
		Iterator<TopPlayer> iter = list.descendingIterator();
		List<String> out = new ArrayList<String>((int) ((float) list.size() / (float) pageLength) + 5);
		StringBuilder build = new StringBuilder(20 * firstLength);
		int i = 1;
		int page = firstLength;
		while (iter.hasNext()) {
			TopPlayer next = iter.next();
			if (i <= 3) {
				build.append("§6");
			} else {
				build.append("§3");
			}
			build.append(i).append(" §0").append(next.playerName).append("§8: ").append(next.level).append("\n");
			i++;
			page--;
			if (page <= 0) {
				out.add(build.toString());
				page = pageLength;
				build = new StringBuilder(20 * pageLength);
			}
		}
		if (build.length() > 3) {
			out.add(build.toString());
		}
		return out;
	}

	public List<FancyMessage> getFancyPages(int firstLength, int pageLength, String prefix, String postfix) {
		if (list.isEmpty()) {
			List<FancyMessage> out = new ArrayList<FancyMessage>(1);
			out.add(new FancyMessage(""));
			return out;
		}
		Iterator<TopPlayer> iter = list.descendingIterator();
		List<FancyMessage> out = new ArrayList<FancyMessage>((int) ((float) list.size() / (float) pageLength) + 5);
		//StringBuilder build = new StringBuilder(20 * firstLength);
		int i = 1;
		int page = firstLength;
		FancyMessage msg = new FancyMessage("");
		while (iter.hasNext()) {
			TopPlayer next = iter.next();
			msg.then(i + " ");
			if (i <= 3) {
				msg.color(ChatColor.GOLD);
				msg.then(next.playerName + "\n").color(ChatColor.BLACK);
				msg.tooltip("§6§l" + i + ". Platz", "§6" + next.playerName, prefix + "§e" + next.level + " §7" + postfix);
			} else {
				msg.color(ChatColor.DARK_AQUA);
				msg.then(next.playerName + "\n").color(ChatColor.BLACK);
				msg.tooltip("§b" + next.playerName, prefix + "§e" + next.level + " §7" + postfix);
			}
			i++;
			page--;
			if (page <= 0) {
				out.add(msg);
				page = pageLength;
				msg = new FancyMessage("");
			}
		}
		if (page != pageLength) {
			out.add(msg);
		}
		return out;
	}

	@SuppressWarnings("ResultOfMethodCallIgnored")
	public void saveToFile(boolean saveList) {
		if (topFolder == null) return;
		File backup = null;
		File file = new File(topFolder, saveList ? LISTFILE : LEVELSFILE);
		if (file.exists()) {
			backup = new File(file.getParent(), file.getName() + "_old");
			file.renameTo(backup);
		}

		DataOutputStream data = null;
		try {
			if (!topFolder.exists()) {
				topFolder.mkdirs();
			}
			file.createNewFile();
			OutputStream stream = new FileOutputStream(file);
			data = new DataOutputStream(stream);

			//data.writeInt(lastLevelInList);
			if (saveList) {
				data.writeLong(timeGenerated);
				data.writeInt(list.size());
				for (TopPlayer out : list) {
					data.writeInt(out.level);
					data.writeLong(out.time);
					data.writeUTF(out.playerName);
					data.writeLong(out.plId.getMostSignificantBits());
					data.writeLong(out.plId.getLeastSignificantBits());
				}
			} else {
				int i = levels.size();
				data.writeInt(i);

				for (MutableInt level : levels) {
					int out = level.intValue();
					boolean large = out >= UNSIGNED_SHORT_LENGTH;
					data.writeBoolean(large);
					if (large) {
						data.writeInt(out);
					} else {
						data.writeShort((short) out);
					}
				}
			}
			data.close();

		} catch (Exception e) {
			P.p.log("§cError writing " + (saveList ? LISTFILE : LEVELSFILE));
			e.printStackTrace();
			if (backup != null) {
				backup.renameTo(file);
			}
		} finally {
			if (data != null) {
				try {
					data.close();
					if (backup != null && backup.exists()) {
						backup.delete();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void loadFromFile() {
		changed = true;
		if (topFolder == null || !new File(topFolder, LEVELSFILE).exists()) {
			levels = new ArrayList<MutableInt>(40);
			if (topFolder == null || !new File(topFolder, LISTFILE).exists()) {
				timeGenerated = System.currentTimeMillis();
				return;
			}
		}

		boolean success = false;
		File levelFile = new File(topFolder, LEVELSFILE);
		File listFile = new File(topFolder, LISTFILE);

		DataInputStream data = null;
		InputStream stream;
		try {
			if (listFile.exists()) {
				stream = new FileInputStream(listFile);
				data = new DataInputStream(stream);

				success = true;
				timeGenerated = data.readLong();
				int size = data.readInt();
				for (int i = 0; i < size; i++) {
					list.add(new TopPlayer(data.readInt(), data.readLong(), data.readUTF(), new UUID(data.readLong(), data.readLong())));
				}
			}

		} catch (Exception e) {
			success = false;
			P.p.log("§cError reading QuestTopList");
			e.printStackTrace();
		} finally {
			if (data != null) {
				try {
					data.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			TopPlayer t = list.first();
			if (t != null) {
				lastLevelInList = t.level;
			}
		}
		data = null;
		if (!success || list.isEmpty()) {
			list.clear();
			timeGenerated = System.currentTimeMillis();
			return;
		}

		try {
			if (levelFile.exists()) {
				stream = new FileInputStream(levelFile);
				data = new DataInputStream(stream);

				int size = data.readInt();
				levels = new ArrayList<MutableInt>(size + 32);
				for (int i = 0; i < size; i++) {
					if (data.readBoolean()) { // large
						levels.add(new MutableInt(data.readInt()));
					} else {
						levels.add(new MutableInt(data.readUnsignedShort()));
					}
				}
			}


		} catch (Exception e) {
			P.p.log("§cError reading TopList");
			e.printStackTrace();
		} finally {
			if (levels == null) {
				levels = new ArrayList<MutableInt>(40);
			}
			if (data != null) {
				try {
					data.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void updateNames(final UUID id, final String name) {
		P.p.getServer().getScheduler().scheduleSyncDelayedTask(P.p, new Runnable() {
			@Override
			public void run() {
				for (TopList tl : registeredTopLists) {
					for (TopPlayer top : tl.list) {
						if (top.plId.equals(id)) {
							top.playerName = name;
							break;
						}
					}
				}
			}
		}, 5);
	}

}
