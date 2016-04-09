package com.snow.menu.Util;

import java.util.Iterator;

import com.snow.menu.Buttons.Button;
import com.snow.menu.Buttons.IButton;
import com.snow.menu.Menu;

public class ButtonIterator<Type extends IButton> implements Iterator<Type> {

	private Menu menu;
	private Button[] buttons;
	private int index = -1;
	private int next = -1;
	private Class<Type> clazz;

	public ButtonIterator(Menu menu) {
		this(menu, null);
	}

	public ButtonIterator(Menu menu, Class<Type> clazz) {
		this.menu = menu;
		this.buttons = menu.getButtons();
		this.clazz = clazz;
		getNext();
	}

	@Override
	public boolean hasNext() {
		return buttons.length > next;
	}

	@Override
	public Type next() {
		getNext();
		return (Type) buttons[index];
	}

	@Override
	public void remove() {
		if (index < 0) throw new IndexOutOfBoundsException("Cannot remove before calling next");
		menu.removeButton(index);
	}

	private void getNext() {
		index = next;
		next++;
		while (buttons.length > next && (buttons[next] == null || (clazz != null && !clazz.isAssignableFrom(buttons[next].getClass())))) {
			next++;
		}
	}
}
