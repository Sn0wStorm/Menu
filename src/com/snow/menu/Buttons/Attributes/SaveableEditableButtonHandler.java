package com.snow.menu.Buttons.Attributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SaveableEditableButtonHandler extends SaveableButtonHandler {

	private EditableButton b;

	public SaveableEditableButtonHandler(EditableButton b) {
		super((SaveableButton) b);
	}

	@Override
	public void setButton(SaveableButton b) {
		if (!(b instanceof EditableButton)) throw new IllegalArgumentException("Not a EditableButton in SaveableEditableButtonHandler");
		this.b = ((EditableButton) b);
	}

	@Override
	public String save_getText() {
		StringBuilder builder = new StringBuilder();
		for (String line : b.getEditHandler().getText()) {
			if (line == null) {
				builder.append("§%");
			} else {
				builder.append(line).append("\n");
			}
		}
		if (builder.length() > 2) {
			return builder.substring(0, builder.length() - 1);
		} else {
			return "";
		}
	}

	@Override
	public void load_text(String text) {
		if (text == null || text.length() < 1) {
			return;
		}
		List<String> lore = new ArrayList<String>();
		for (String page : text.split("§%")) {
			if (page.length() > 0) {
				Collections.addAll(lore, page.split("\n"));
				lore.add(null);
			}
		}
		if (!lore.isEmpty()) {
			lore.remove(lore.size() - 1);
			b.getEditHandler().setText(lore);
			b.getEditHandler().updateLore();
		}
	}
}
