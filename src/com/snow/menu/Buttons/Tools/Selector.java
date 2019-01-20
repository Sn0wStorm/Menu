package com.snow.menu.Buttons.Tools;

import java.util.UUID;

import com.snow.menu.Buttons.Button;


/*
  When the Player has selected a Button this will be called
 */

public interface Selector {

	void selected(UUID player, Button button);
}
