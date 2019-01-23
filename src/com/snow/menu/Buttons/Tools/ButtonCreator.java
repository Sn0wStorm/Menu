package com.snow.menu.Buttons.Tools;

import com.snow.menu.Buttons.Button;

/*
  An Object that can create or be converted into a Button
  Used for ListMenu
 */
public interface ButtonCreator {

	// Create a Button from this Object
	// The Index is at which index in the List Menu this Button is being created
	// the index can usually be ignored
	Button createButton(int index);
}
