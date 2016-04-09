package com.snow.menu.Buttons.Attributes;

import com.snow.menu.Buttons.IButton;

/*
  Having a Button implement this interface makes it not movable (static) in Menus that are otherwise editable
  They also may not be deleted anymore
  Useful for Buttons that should always be at the same place, yet other Buttons may move
  Implementing this Interface is the same as setting movable to false on the Button
 */
public interface ImmovableButton extends IButton {
}
