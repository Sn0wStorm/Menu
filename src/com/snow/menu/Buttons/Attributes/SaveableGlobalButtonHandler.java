package com.snow.menu.Buttons.Attributes;

import com.snow.menu.Buttons.GlobalButton;
import com.snow.menu.Menus.Attributes.SaveableMenu;


/*
  Use This also for Editable AND Global Buttons
 */

public class SaveableGlobalButtonHandler extends SaveableButtonHandler {

	private GlobalButton b;
	private SaveableEditableButtonHandler edit;

	public SaveableGlobalButtonHandler(GlobalButton b) {
		super(b);
	}

	@Override
	public void setButton(SaveableButton b) {
		if (!(b instanceof GlobalButton)) throw new IllegalArgumentException("Not a GlobalButton in SaveableGlobalButtonHandler");
		this.b = ((GlobalButton) b);
		if (b instanceof EditableButton) {
			edit = new SaveableEditableButtonHandler(((EditableButton) b));
		}
	}

	public GlobalButton getButton() {
		return b;
	}

	@Override
	public int save_getSlot() {
		return b.getSlot();
	}

	@Override
	public int save_getButtonType() {
		return getButton().getSqlButtonType();
	}

	@Override
	public String save_getText() {
		if (edit != null) {
			return edit.save_getText();
		} else {
			return super.save_getText();
		}
	}

	@Override
	public void load_text(String text) {
		if (edit != null) {
			edit.load_text(text);
		} else {
			super.load_text(text);
		}
	}

	@Override
	public void load_menu(SaveableMenu menu, int slot) {
		if (menu != null) {
			getButton().load_slot(slot);
			menu.addButton(getButton(), slot);
		}
	}

	@Override
	public void load_buttonType(int buttonType) {
		if (buttonType == 1 && getButton() instanceof EditableButton) {
			((EditableButton) getButton()).getEditHandler().nameEditable = true;
		} else {
			super.load_buttonType(buttonType);
		}
	}
/*
	@Override
	public void loadDone() {
		b.setSaved(true);
	}*/
}
