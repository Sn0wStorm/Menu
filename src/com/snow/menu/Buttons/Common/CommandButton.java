package com.snow.menu.Buttons.Common;

import com.snow.menu.Buttons.Button;
import com.snow.menu.MenuView;
import com.snow.menu.P;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;


/*
  Button that executes a Command on click
 */
public class CommandButton extends Button {
	public static final boolean CONSOLE = true;
	public static final boolean PLAYER = false;


	protected CommandOptions options;

	public CommandButton(Material type, CommandOptions options) {
		super(type);
		this.options = options;
	}

	public CommandButton(Material type, CommandOptions options, String name) {
		super(type, name);
		this.options = options;
	}

	public CommandButton(Material type, CommandOptions options, String name, String... lore) {
		super(type, name, lore);
		this.options = options;
	}

	public CommandButton(ItemStack item, CommandOptions options) {
		super(item);
		this.options = options;
	}

	public CommandOptions getOptions() {
		return options;
	}

	public CommandButton setOptions(CommandOptions options) {
		this.options = options;
		return this;
	}

	@Override
	public void onClick(InventoryClickEvent event, MenuView view) {
		if (options.closeMenu) {
			event.getWhoClicked().closeInventory();
		}
		if (options.asConsole == CONSOLE) {
			P.p.getServer().dispatchCommand(P.p.getServer().getConsoleSender(), options.command);
		} else {
			P.p.getServer().dispatchCommand(event.getWhoClicked(), options.command);
		}
	}


	public static class CommandOptions {
		private String command;
		public boolean asConsole;
		public boolean closeMenu;

		public CommandOptions(String command, boolean asConsole) {
			setCommand(command);
			this.asConsole = asConsole;
		}

		public CommandOptions(String command, boolean asConsole, boolean closeMenu) {
			setCommand(command);
			this.asConsole = asConsole;
			this.closeMenu = closeMenu;
		}

		public String getCommand() {
			return command;
		}

		public void setCommand(String command) {
			if (command == null) {
				throw new IllegalArgumentException("Command cant be null");
			}
			this.command = command;
		}
	}
}
