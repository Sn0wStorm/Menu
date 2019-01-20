package com.snow.menu;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;

import org.apache.commons.lang.math.NumberUtils;

import com.snow.testmenu.Menus.MLostMain;
import com.snow.menu.Book.BookButton;
import com.snow.menu.Book.BookListener;
import com.snow.menu.Book.ClickListener;
import com.snow.menu.Book.ItemReplacer;
import com.snow.menu.Menus.MItemSelect;
import com.snow.menu.Saving.SqlButtonSaver;
import com.snow.menu.Saving.XMan;

import java.sql.SQLException;

public class P extends JavaPlugin {

	public static P p;
	public MenuListener menuListener;
	public BookListener bookListener;

	public MLostMain mainMenu;
	//public int id = 0;

	private PluginRegistry registry = new PluginRegistry();

	@Override
	public void onEnable() {
		p = this;

		try {
			Class.forName( "net.minecraft.server.v1_13_R2.EntityLiving" );
		} catch(ClassNotFoundException e) {
			log("§cMenu konnte nicht Aktiviert werden, da es Serverversion v1_13_R2 voraussetzt!");
			p = null;
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		// Listeners
		menuListener = new MenuListener();
		bookListener = new BookListener();
		BookButton.clickListener = new ClickListener();
		//getCommand("LostMenu").setExecutor(new CommandListener());

		getServer().getPluginManager().registerEvents(menuListener, this);
		getServer().getPluginManager().registerEvents(bookListener, this);
		getCommand("testmenu").setExecutor(new CommandListener());
		getCommand("bcls").setExecutor(BookButton.clickListener);

		readConfig();
		hookPlugins();
		getServer().getScheduler().scheduleSyncDelayedTask(this, this::initBaseMenus, 1);
		loadData();

		// Heartbeat
		//p.getServer().getScheduler().runTaskTimer(p, new FlyRunnable(), 1, 1);

		//p.convert();

		this.log(this.getDescription().getName() + " enabled!");
	}

	@Override
	public void onDisable() {
		if (p != null) {
			ItemReplacer.revertAll();
			clearData();
			try {
				SqlButtonSaver.closeConnection();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		this.log(this.getDescription().getName() + " disabled!");
	}

	public PluginRegistry getRegistry() {
		return registry;
	}

	public void reload() {

	}

	public void clearData() {
		HandlerList.unregisterAll(p);
		getServer().getScheduler().cancelTasks(P.p);
	}

	public void loadData() {
		getServer().getScheduler().runTaskLater(this, new Runnable() {
			@Override
			public void run() {
				SqlButtonSaver.loadTask();
			}
		}, 1);

	}

	public void readConfig() {
		SqlButtonSaver.init();
	}

	public void hookPlugins() {
		if (getServer().getPluginManager().isPluginEnabled("XServer")) {
			XMan.init();
		} else {
			log("&cERROR: XServer nicht aktiviert!");
		}
	}

	public void initBaseMenus() {
		mainMenu = new MLostMain();
		MItemSelect.allItems = new MItemSelect();
	}

	/*@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (sender instanceof Player) {
			if (!sender.isOp()) {
				sender.sendMessage("§cDu hast keine Rechte dazu");
				return true;
			}
		}


		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				reload();
				sender.sendMessage("Reloaded BookTextApi");
				return true;
			} else if (args[0].equals("bkghz")) {
				((Player) sender).setGameMode(GameMode.CREATIVE);
			} else if (args[0].equals("sdgdf")) {
				((Player) sender).sendTitle("§aQuest Angenommen", "Du hast eine Quest Angenommen");
				return true;
			} else if (args[0].equals("oern")) {
				if (args.length >= 3) {
					testZahl = parseInt(args[2]);
				}
			} else {
				return true;
			}
		}

		BookText test = new BookText();
		FancyMessage msg = new FancyMessage("Dies ist ein test\n\n");
		msg.color(ChatColor.BLUE);
		msg.then("Hier klicken alt!\n").color(ChatColor.DARK_RED).style(ChatColor.UNDERLINE).tooltip("Das ist ein test", "könnte klappen", "du bekommst gamemode!", "§4Aber das Buch schließt sich :(").command("/gamemode 1");
		msg.then("Hier klicken neu!\n").color(ChatColor.DARK_RED).style(ChatColor.UNDERLINE).tooltip("Das ist ein test", "könnte klappen", "du bekommst gamemode!").command("/booktextapi bkghz");
		msg.then("123test123 ein langer text weil es kann sein das nicht automatisch linebreaks eingefügt werden\n").color(ChatColor.BLACK).itemTooltip(new ItemStack(Material.DIAMOND_SWORD, 1, (short) 20));
		msg.then("Zombie\n").color(ChatColor.RED);
		msg.then("Nächste Seite\n\n\n").color(ChatColor.GRAY).style(ChatColor.UNDERLINE).page(2).tooltip("Nächste Seite öffnen");
		msg.then("Annehmen").color(ChatColor.DARK_GREEN).style(ChatColor.UNDERLINE, ChatColor.BOLD).tooltip("Quest annehmen").command("/booktextapi sdgdf");
		//msg.then("  ");
		msg.then("Ablehnen\n").color(ChatColor.RED).style(ChatColor.UNDERLINE, ChatColor.BOLD).tooltip("Quest ablehnen").command("/booktextapi ghjhg");;
		test.addText(msg);

		msg = new FancyMessage("Zweite seite\n");
		msg.color(ChatColor.GRAY).style(ChatColor.BOLD);
		msg.then("Test Zahl:").color(ChatColor.GRAY).tooltip("Klicken zum ändern").suggest("/bta oern Zahl_Eingeben: ");
		msg.then(" " + testZahl).color(ChatColor.YELLOW).tooltip("Klicken zum ändern").suggest("/bta oern Zahl_Eingeben: ");
		test.addText(msg);

		test.displayTo(((Player) sender));


		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("reload")) {
				reload();
				sender.sendMessage("Reloaded BookTextApi");
				return true;
			}
		}
		return true;
	}*/

	public int parseInt(String string) {
		return NumberUtils.toInt(string, 0);
	}

	public void msg(CommandSender sender, String msg) {
		sender.sendMessage(color(msg));
	}

	public void log(String msg) {
		this.msg(Bukkit.getConsoleSender(), "[LostMenu] " + msg);
	}

	public String color(String msg) {
		if (msg != null) {
			msg = ChatColor.translateAlternateColorCodes('&', msg);
		}

		return msg;
	}

	public String removeColor(String string) {
		return ChatColor.stripColor(string);
	}

}

