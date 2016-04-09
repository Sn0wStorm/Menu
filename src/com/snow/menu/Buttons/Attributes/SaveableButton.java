package com.snow.menu.Buttons.Attributes;

import com.snow.menu.Buttons.IButton;

public interface SaveableButton extends IButton {

	SaveableButtonHandler getSaveHandler();
	void setSaveHandler(SaveableButtonHandler save);

	// Called when the Button was loaded
	void load();
}
