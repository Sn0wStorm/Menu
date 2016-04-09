package com.snow.menu.Saving;

import com.snow.menu.P;

public class XMan {

	public static boolean init;

	public static void init() {
		try {
			XSender.x = (de.mickare.xserver.XServerPlugin) P.p.getServer().getPluginManager().getPlugin("XServer");
			XSender.x.getManager().getEventHandler().registerListenerUnsafe(P.p, new XListener());
			init = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
