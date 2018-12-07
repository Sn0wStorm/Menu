package com.snow.menu.Buttons.TopList;

import java.util.UUID;


public class TopPlayer implements Comparable<TopPlayer> {


	public int level;
	public long time;
	public final UUID plId;
	public String playerName;


	public TopPlayer(int level, long time, String player, UUID uuid) {
		this.level = level;
		this.time = time;
		playerName = player;
		plId = uuid;
	}

	@Override
	public int compareTo(TopPlayer tp) {
		if (tp.level == level) {
			if (tp.time > time) {
				return 1;
			} else if (tp.time < time) {
				return -1;
			} else {
				return plId.equals(tp.plId) ? 0 : 1;
			}
		} else if (level > tp.level) {
			return level - tp.level + 1;
		} else {
			return level - tp.level - 1;
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null) return false;

		if (o instanceof TopPlayer) {
			TopPlayer tp = (TopPlayer) o;
			return level == tp.level && time == tp.time && plId.equals(tp.plId);
		}
		return false;
	}
}
