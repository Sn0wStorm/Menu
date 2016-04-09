package com.snow.menu.Buttons.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.snow.menu.ButtonEditor;
import com.snow.menu.Buttons.Button;
import com.snow.menu.MenuView;
import com.snow.menu.P;


public class EditableButtonHandler {

	private EditableButton b;
	public boolean nameEditable; // If the item should be renameable
	public String[] prefix; // Uneditable text at the beginning
	public String[] suffix; // Uneditable text at the end
	public String[] suffixCanEdit; // Additional uneditable text at the end, visible when player can edit button
	public boolean loreHasCanEdit = false; // If the lore contains the suffixCanEdit right now

	// Lines of text, add a null element as page separator
	// Text must be with & as color code for editing and then translated later
	public List<String> text = new ArrayList<String>();

	public EditableButtonHandler(EditableButton b) {
		this.b = b;
		setDefaultSuffix();
	}

	// Set the prefix and properly color translate it
	public void setPrefix(String... arr) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = Button.loreColor(arr[i]);
		}
		prefix = arr;
	}

	// Set the suffix and properly color translate it
	public void setSuffix(String... arr) {
		for (int i = 0; i < arr.length; i++) {
			arr[i] = Button.loreColor(arr[i]);
		}
		suffix = arr;
	}

	public String[] getPrefix() {
		return prefix;
	}

	public String[] getSuffix() {
		return suffix;
	}

	public String[] getSuffixCanEdit() {
		return suffixCanEdit;
	}

	public void setSuffixCanEdit(String[] suffixCanEdit) {
		this.suffixCanEdit = suffixCanEdit;
	}

	public boolean hasLoreCanEdit() {
		return loreHasCanEdit;
	}

	public void setLoreHasCanEdit(boolean loreHasCanEdit) {
		this.loreHasCanEdit = loreHasCanEdit;
	}

	public List<String> getText() {
		return text;
	}

	public void setText(List<String> text) {
		this.text = text;
	}

	public void setDefaultSuffix() {
		suffixCanEdit = new String[] {"", "§8Doppelt Rechtsklick zum bearbeiten"};
	}

	// Updates the Button lore from prefix, text and suffix
	public void updateLore() {
		int size = 0;
		if (text != null) size += text.size();
		if (suffix != null) size += suffix.length;
		if (prefix != null) size += prefix.length;
		if (loreHasCanEdit && suffixCanEdit != null) size += suffixCanEdit.length;
		if (size == 0) return;

		List<String> lore = new ArrayList<String>(size);
		if (prefix != null) {
			Collections.addAll(lore, prefix);
		}
		if (text != null) {
			for (String line : text) {
				if (line != null) {
					lore.add(Button.loreColor(line));
				}
			}
		}
		if (suffix != null) {
			Collections.addAll(lore, suffix);
		}
		if (loreHasCanEdit && suffixCanEdit != null) {
			Collections.addAll(lore, suffixCanEdit);
		}
		b.setLore(lore);
	}

	// Creates editable Text from the Lore of the Item
	public void initFromLore() {
		this.text = b.getLore();
	}

	/*public ItemStack getEditable(ItemStack item) {
		if (suffixCanEdit != null && suffixCanEdit.length > 0) {
			item = item.clone();
			List<String> lore = b.getLore();
			if (lore == null) {
				lore = new ArrayList<String>();
			}
			Collections.addAll(lore, suffixCanEdit);
			ItemMeta meta = item.getItemMeta();
			meta.setLore(lore);
			item.setItemMeta(meta);
		}
	}*/

	public void updateEditable(Player player) {
		if (b.canEdit(player) ) {
			if (!loreHasCanEdit) {
				loreHasCanEdit = true;
				if (suffixCanEdit != null && suffixCanEdit.length > 0) {
					List<String> lore = b.getLore();
					if (lore == null) {
						lore = new ArrayList<String>();
					}
					Collections.addAll(lore, suffixCanEdit);
					b.setLore(lore);
				}
			}
		} else if (loreHasCanEdit) {
			loreHasCanEdit = false;
			updateLore();
		}
	}

	public void edit(Player player, MenuView view) {
		player.closeInventory();

		/*for (String line : text) {
			if (line == null) {
				P.p.log("NULL");
			} else {
				Bukkit.getLogger().warning(line);
			}
		}*/

		ButtonEditor editor = new ButtonEditor(view, b);

		mergeToPages(editor);

		editor.displayTo(player);
	}

	public void mergeToPages(ButtonEditor edit) {
		StringBuilder pagebuilder = new StringBuilder();
		if (nameEditable) {
			pagebuilder.append(b.getName().replaceAll("§", "&"));
		}
		for (String line : text) {
			if (line == null) {
				edit.addPage(pagebuilder.toString());
				P.p.log(pagebuilder.toString());
				pagebuilder = new StringBuilder();
			} else {
				if (pagebuilder.length() > 0) {
					pagebuilder.append("\n");
				}
				pagebuilder.append(line);
			}
		}

		Bukkit.getLogger().info(" --- ");
		Bukkit.getLogger().warning(pagebuilder.toString());

		edit.addPage(pagebuilder.toString());
	}
}
