package com.snow.menu.Saving;

import java.io.IOException;

import de.mickare.xserver.Message;
import de.mickare.xserver.XServerPlugin;
import de.mickare.xserver.net.XServer;

public class XSender {
	public static XServerPlugin x;

	public static void sendStopSaving() {
		Message msg = x.getManager().createMessage("Menu_StopSaving", new byte[0]);
		broadcast(msg);
	}

	public static void sendWillSave() {
		Message msg = x.getManager().createMessage("Menu_WillSave", new byte[0]);
		broadcast(msg);
	}

	public static void sendSaved() {
		Message msg = x.getManager().createMessage("Menu_Saved", new byte[0]);
		broadcast(msg);
	}

	public static void broadcast(Message msg) {
		for (XServer server : x.getManager().getServers()) {
			if (server == x.getManager().getHomeServer()) return;
			try {
				server.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
