package com.snow.menu.Saving;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.scheduler.BukkitTask;

import de.mickare.xserver.Message;
import de.mickare.xserver.XServerListener;
import de.mickare.xserver.annotations.XEventHandler;
import de.mickare.xserver.events.XServerMessageIncomingEvent;

import com.snow.menu.P;

public class XListener implements XServerListener {

	public static BukkitTask willSaveTimeout;

	@XEventHandler(sync = false)
	public void onMessage(XServerMessageIncomingEvent event) {
		Message msg = event.getMessage();

		if (!msg.getSubChannel().startsWith("Menu")) return;

		P.p.log("Msg incoming: " + msg.getSubChannel());

		DataInputStream in = new DataInputStream(new ByteArrayInputStream(msg.getContent()));
		String channel = msg.getSubChannel();

		if (channel.equals("Menu_WillSave")) {
			if (SqlButtonSaver.state > 0 && SqlButtonSaver.state < 3) {
				P.p.log("We are also saving, stop them!");
				XSender.sendStopSaving();
				return;
			}
			SqlButtonSaver.state = 4;

			willSaveTimeout = P.p.getServer().getScheduler().runTaskLater(P.p, new Runnable() {
				@Override
				public void run() {
					if (SqlButtonSaver.state == 4) {
						SqlButtonSaver.state = 0;
					}
					willSaveTimeout = null;
				}
			}, 200);

		} else if (channel.equals("Menu_StopSaving")) {
			if (SqlButtonSaver.state == 1 || SqlButtonSaver.state == 2 || SqlButtonSaver.state == 4){
				SqlButtonSaver.state = 0;
				SqlButtonSaver.stopTask();
			}
			stopWillSaveTimeout();

		} else if (channel.equals("Menu_Saved")) {
			stopWillSaveTimeout();
			if (SqlButtonSaver.state == 3) {
				SqlButtonSaver.stopTask();
				SqlButtonSaver.state = 4;
			}
			if (SqlButtonSaver.state != 4) return;

			SqlButtonSaver.loadTask();

		} else {
			P.p.log("Ignoring unknown Message!");
		}
		try {
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void stopWillSaveTimeout() {
		if (willSaveTimeout == null) return;
		P.p.getServer().getScheduler().cancelTask(willSaveTimeout.getTaskId());
	}
}
