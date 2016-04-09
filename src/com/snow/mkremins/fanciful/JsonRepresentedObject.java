package com.snow.mkremins.fanciful;

import java.io.IOException;

import com.google.gson.stream.JsonWriter;

/**
 * Represents an object that can be serialized to a JSON writer instance.
 *
 * From fancyful: Lightweight library offering pleasant chat message formatting for Bukkit plugins.
 * A way to get at the good stuff offered by Minecraft 1.7's new chat protocol without dropping down to raw JSON.
 * https://github.com/mkremins/fanciful
 */
interface JsonRepresentedObject {

	/**
	 * Writes the JSON representation of this object to the specified writer.
	 * @param writer The JSON writer which will receive the object.
	 * @throws IOException If an error occurs writing to the stream.
	 */
	public void writeJson(JsonWriter writer) throws IOException;
	
}
