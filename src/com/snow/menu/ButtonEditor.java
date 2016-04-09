package com.snow.menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEditBookEvent;

import com.snow.menu.Book.EditableBookText;
import com.snow.menu.Buttons.Attributes.EditableButton;
import com.snow.menu.Buttons.Attributes.EditableButtonHandler;
import com.snow.menu.Buttons.Button;

  /*
    Uses Book Text to edit an EditableButton
  */

public class ButtonEditor extends EditableBookText {

	private MenuView view;
	private EditableButton button;
	private EditableButtonHandler edit;

	public ButtonEditor(MenuView fromView, EditableButton button) {
		view = fromView;
		this.button = button;
		edit = button.getEditHandler();
	}

	@Override
	public void onEdit(PlayerEditBookEvent event, List<String> pages) {
		P.p.getLogger().info("Edited");
		for (String page : pages) {
			P.p.getLogger().warning("page");
			P.p.getLogger().warning(page);
		}
		List<String> lore = new ArrayList<String>();

		addPrefix(lore);
		addText(lore, pages);
		addSuffix(lore);

		List<String> oldLore = button.getLore();
		button.setLore(lore);

		button.wasEdited(event.getPlayer(), lore, oldLore);

		button.update(view, event.getPlayer());
		view.showAgain(event.getPlayer(), null);

	}

	private void addPrefix(List<String> lore) {
		if (edit.prefix != null) {
			Collections.addAll(lore, edit.prefix);
		}
	}

	private void addText(List<String> lore, List<String> pages) {
		edit.text = new ArrayList<String>();
		boolean firstLine = true;
		for (String page : pages) {
			if (!firstLine) {
				if (page.isEmpty()) {
					continue;
				}
				edit.text.add(null);
			}
			for (String line : page.split("\n")) {
				if (firstLine) {
					firstLine = false;
					if (edit.nameEditable) {
						button.setName(Button.nameColor(line));
						continue;
					}
				}
				lore.add(Button.loreColor(line));
				edit.text.add(line);
			}
		}
	}

	private void addSuffix(List<String> lore) {
		if (edit.suffix != null) {
			Collections.addAll(lore, edit.suffix);
		}
	}

	@Override
	public void wasRemoved(Player player, boolean hadOpen) {
		if (hadOpen) {
			P.p.log("the player cancelled :(");
		} else {
			P.p.log("the player failed to open the book :(");
		}
	}

}
