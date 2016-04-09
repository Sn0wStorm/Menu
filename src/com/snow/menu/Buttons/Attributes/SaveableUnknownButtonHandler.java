package com.snow.menu.Buttons.Attributes;

import org.bukkit.configuration.file.FileConfiguration;

import com.snow.menu.Buttons.GlobalButton;

public class SaveableUnknownButtonHandler extends SaveableGlobalButtonHandler {

	private String clazz;
	private FileConfiguration extra;
	private String text;
	private int buttonType;

	public SaveableUnknownButtonHandler(GlobalButton b, String clazz) {
		super(b);
		this.clazz = clazz;
	}

	@Override
	public FileConfiguration save_getExtra() {
		return extra;
	}

	@Override
	public int save_getButtonType() {
		return buttonType;
	}

	@Override
	public String save_getClassName() {
		return clazz;
	}

	@Override
	public String save_getText() {
		return text;
	}

	@Override
	public void load_text(String text) {
		this.text = text;
	}

	@Override
	public void load_extra(FileConfiguration extra) {
		this.extra = extra;
	}

	@Override
	public void load_buttonType(int buttonType) {
		this.buttonType = buttonType;
	}


}
