package com.snow.menu.Menus.Attributes;

import com.snow.menu.IMenu;

/**
 * When a Menu implements this, it can not be openend by using the Back button
 * Clicking the Back button will go to the Menu opened prior to this
 * Useful for Menus that should only show once or at certain times, like Item Selector Menus etc.
 * Implementing this is the same as setting noBack to true in the Menu
 */
public interface NoBackMenu extends IMenu {
}
