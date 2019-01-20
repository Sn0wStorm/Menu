package com.snow.menu.Saving;

import java.io.File;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitTask;

import com.snow.menu.Buttons.Attributes.SaveableButton;
import com.snow.menu.Buttons.Attributes.SaveableButtonHandler;
import com.snow.menu.Buttons.GlobalButton;
import com.snow.menu.Menus.Attributes.SaveableMenu;
import com.snow.menu.Menus.Attributes.SaveableMenuHandler;
import com.snow.menu.P;

public class SqlButtonSaver implements Runnable {
	private static Connection connection;
	private static String connect;
	private static String user;
	private static String password;

	private static BukkitTask task;
	public static int state; // 0 = ready, 1 = waiting to save, 2 = saving, 3 = loading, 4 = other server is saving

	private boolean save;

	private SqlButtonSaver(boolean save) {
		this.save = save;
	}

	public static void saveTask() {
		if (task == null && state == 0) {
			state = 2;
			task = P.p.getServer().getScheduler().runTaskAsynchronously(P.p, new SqlButtonSaver(true));
		}
	}

	public static void loadTask() {
		if (task == null) {
			if (state == 2 || state == 3) return;
			state = 3;
			task = P.p.getServer().getScheduler().runTaskAsynchronously(P.p, new SqlButtonSaver(false));
		}
	}

	public static void stopTask() {
		if (task != null) {
			P.p.getServer().getScheduler().cancelTask(task.getTaskId());
			task = null;
		}
	}


	@Override
	public void run() {
		try {
			if (save) {
				if (XMan.init) {
					P.p.log("saving, with XServer, sending WillSave, sleeping");
					state = 1;
					XSender.sendWillSave();
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (task == null || state == 0) {
						P.p.log("Could not save, another Server is already Saving");
						return;
					}
				}
				P.p.log("saving");
				state = 2;

				task = P.p.getServer().getScheduler().runTask(P.p, () -> {
					try {
						save();
					} catch (Exception e) {
						e.printStackTrace();
						task = null;
						state = 0;
					}
				});

			} else {
				task = P.p.getServer().getScheduler().runTaskAsynchronously(P.p, this::loading);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// Main Thread, Do all data collecting here
	private void save() {
		int i = 0;
		final SaveDataB[] b = new SaveDataB[GlobalButton.getButtons().size()];
		for (GlobalButton button : GlobalButton.getButtons()) {
			try {
				SaveableButtonHandler s = button.getSaveHandler();
				if (s.saveStart()) {
					FileConfiguration c = s.save_getExtra();
					if (c != null && c.getKeys(false).isEmpty()) {
						c = null;
					}
					b[i] = new SaveDataB(
							s.save_getType(),
							s.save_getName(),
							s.save_getAmount(),
							s.save_getButtonType(),
							s.save_getClassName(),
							s.save_getMenuId(),
							s.save_getSlot(),
							s.save_getText(),
							c == null ? null : c.saveToString());

					s.saveDone();

					i++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		final SaveDataM[] m = new SaveDataM[SaveableMenuHandler.getMenus().size()];
		i = 0;

		for (SaveableMenuHandler menu : SaveableMenuHandler.getMenus()) {
			try {
				if (menu.saveStart()) {
					FileConfiguration c = menu.save_getExtra();
					if (c != null && c.getKeys(false).isEmpty()) {
						c = null;
					}

					m[i] = new SaveDataM(
							menu.save_getName(),
							menu.save_getClassName(),
							menu.save_getSize(),
							c == null ? null : c.saveToString());

					menu.saveDone();
					i++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		task = P.p.getServer().getScheduler().runTaskAsynchronously(P.p, () -> saving(b, m));
	}

	// Async thread, do writing to disk, etc
	// No Menus or Buttons should be touched here
	private void saving(SaveDataB[] bData, SaveDataM[] mData) {
		try {
			if (!checkConnection()) {
				if (!openConnection()) {
					return;
				}
			}

			Statement statement = connection.createStatement();
			statement.executeUpdate("TRUNCATE zmenu_Buttons;");

			int id = 0;
			for (SaveDataB b : bData) {
				if (b != null) {
					PreparedStatement ps = connection.prepareStatement("INSERT INTO zmenu_Buttons (id, mat, name, amount, type, clazz, menu, slot, text, extra) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
					ps.setInt(1, id);
					ps.setString(2, b.a);
					ps.setString(3, b.b);
					ps.setInt(4, b.d);
					ps.setInt(5, b.e);
					ps.setString(6, b.f);
					ps.setInt(7, b.g);
					ps.setInt(8, b.h);
					ps.setString(9, b.i);
					ps.setString(10, b.j);

					ps.executeUpdate();
					id++;
				}
			}

			statement = connection.createStatement();
			statement.executeUpdate("TRUNCATE zmenu_CustMenus;");

			id = 0;
			for (SaveDataM m : mData) {
				if (m != null) {
					PreparedStatement ps = connection.prepareStatement("INSERT INTO zmenu_CustMenus (id, name, clazz, size, extra) VALUES (?, ?, ?, ?, ?);");
					ps.setInt(1, id);
					ps.setString(2, m.a);
					ps.setString(3, m.b);
					ps.setInt(4, m.c);
					ps.setString(5, m.d);

					ps.executeUpdate();
					id++;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (XMan.init) {
				P.p.log("Send Saved");
				XSender.sendSaved();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		state = 0;
		task = null;
	}

	// Async for Data Reading
	// No Buttons and Menus should be touched
	private void loading() {
		try {
			if (!checkConnection()) {
				if (!openConnection()) {
					return;
				}
			}

			final List<SaveDataB> bData = new ArrayList<>();
			final List<SaveDataM> mData = new ArrayList<>();

			Statement statement = connection.createStatement();
			ResultSet res = statement.executeQuery("SELECT * FROM zmenu_CustMenus;");
			while(res.next()) {
				mData.add(new SaveDataM(res.getString("name"), res.getString("clazz"), res.getInt("size"), res.getString("extra")));
			}
			res.close();

			statement = connection.createStatement();
			res = statement.executeQuery("SELECT * FROM zmenu_Buttons;");
			while(res.next()) {
				bData.add(new SaveDataB(res.getString("mat"), res.getString("name"), res.getInt("amount"), res.getInt("type"), res.getString("clazz"),
						res.getInt("menu"), res.getInt("slot"), res.getString("text"), res.getString("extra")));
			}
			res.close();

			P.p.getServer().getScheduler().runTask(P.p, () -> {
				try {
					load(bData, mData);
				} catch (Exception e) {
					e.printStackTrace();
				}
				state = 0;
				task = null;
			});

		} catch (Exception e) {
			e.printStackTrace();
			task = null;
			state = 0;
		}
	}

	// Main Task loading
	private void load(List<SaveDataB> bData, List<SaveDataM> mData) {
		List<SaveableMenu> loadList = new ArrayList<>();
		for (SaveDataM m : mData) {
			loadList.add(SaveableMenuHandler.load(m.a, m.b, m.c, m.d, true));
		}

		for (SaveDataB b : bData) {
			int menuId = b.g;
			if (loadList.size() <= menuId) {
				menuId = -1;
			}
			loadButton(b.a, b.b, b.d, b.e, b.f, menuId == -1 ? null : loadList.get(menuId), b.h, b.i, b.j);
		}
	}

	// The containing Menu must have been loaded before
	public static void loadButton(String mat, String name, int amount, int buttonType, String clazz, SaveableMenu menu, int slot, String text, String extra) {
		if (mat == null || clazz == null) {
			return;
		}

		P.p.log("loading Button: " + mat + "§r, " + clazz + " menu null? " + (menu == null ? "null" : menu.getName()));

		Material type = Material.getMaterial(mat);
		if (type == null) return;

		try {
			Class<? extends SaveableButton> buttonClass = Class.forName(clazz).asSubclass(SaveableButton.class);
			SaveableButton b = buttonClass.getConstructor(Material.class, String.class).newInstance(type, name);
			SaveableButtonHandler s = b.getSaveHandler();

			s.load_amount(amount);
			s.load_buttonType(buttonType);
			s.load_menu(menu, slot);
			s.load_text(text);

			FileConfiguration c = null;
			if (extra != null) {
				try {
					c = YamlConfiguration.loadConfiguration(new StringReader(extra));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (c == null) {
				c = new YamlConfiguration();
			}
			s.load_extra(c);

			s.loadDone();

		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			P.p.log("Loading as Unknown Button: " + name);
			UnknownButton b = new UnknownButton(type, name, clazz);
			b.load(menu, slot, extra, text, buttonType);
		} catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException | ClassCastException e) {
			e.printStackTrace();
		}
	}



	public static boolean init() {
		File file = new File(P.p.getDataFolder(), "config.yml");
		if (!file.exists()) {
			P.p.saveDefaultConfig();
		}
		FileConfiguration config = YamlConfiguration.loadConfiguration(file);
		String host = config.getString("MySql.host", null);
		String port = config.getString("MySql.port", null);
		user = config.getString("MySql.user", null);
		String database = config.getString("MySql.database", null);
		password = config.getString("MySql.password", null);

		if (host == null || port == null || user == null || database == null || password == null) {
			P.p.getLogger().severe("Mysql settings not correctly defined!");
			return false;
		}

		try {
			Class.forName("com.mysql.jdbc.Driver");
			String str = "jdbc:mysql://" + host + ":" + port + "/" + database;
			connection = DriverManager.getConnection(str, user, password);

			Statement statement = connection.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS zmenu_Buttons (" +
					"id INT, " +
					"mat VARCHAR(127), " +
					"name VARCHAR(127), " +
					"amount INT, " +
					"type TINYINT UNSIGNED, " +
					"clazz VARCHAR(127), " +
					"menu INT, " +
					"slot TINYINT UNSIGNED, " +
					"text TEXT, " +
					"extra TEXT, " +
					"PRIMARY KEY (id));");
			statement = connection.createStatement();
			statement.execute("CREATE TABLE IF NOT EXISTS zmenu_CustMenus (" +
					"id INT, " +
					"name VARCHAR(127), " +
					"clazz VARCHAR(127), " +
					"size TINYINT UNSIGNED, " +
					"extra TEXT, " +
					"PRIMARY KEY (id));");

			connect = str;
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public static boolean openConnection() {
		if (connect == null) {
			return false;
		}
		try {
			connection = DriverManager.getConnection(connect, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean checkConnection() throws SQLException {
		return connection != null && !connection.isClosed();
	}

	synchronized public static boolean closeConnection() throws SQLException {
		if (connection == null) {
			return false;
		}
		connection.close();
		return true;
	}

}
