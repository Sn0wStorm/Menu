package com.snow.menu.Util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.PacketPlayOutAttachEntity;

public class NMSUtil {


	public static ItemStack setGlowing(ItemStack item, boolean glowing){
		net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound tag = null;
		if (!nmsStack.hasTag()) {
			if (!glowing) {
				return item;
			}
			tag = new NBTTagCompound();
			nmsStack.setTag(tag);
		}
		if (tag == null) tag = nmsStack.getTag();

		if (glowing) {
			NBTTagList ench = new NBTTagList();
			tag.set("ench", ench);
		} else {
			tag.remove("ench");
		}
		nmsStack.setTag(tag);
		return CraftItemStack.asCraftMirror(nmsStack);
	}

	public static ItemStack forceAmount(ItemStack item, int amount) {
		net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		nmsStack.count = amount;
		return CraftItemStack.asCraftMirror(nmsStack);
	}

	public static void testStackSize() {
		Items.LAVA_BUCKET.c(64);
		Items.APPLE.c(127);
	}

	public static void leash(Entity e1, Entity e2, Player receiver) {
		PacketPlayOutAttachEntity packet = new PacketPlayOutAttachEntity(1, ((CraftEntity) e1).getHandle(), ((CraftEntity) e2).getHandle());
		((CraftPlayer) receiver).getHandle().playerConnection.sendPacket(packet);
	}

	public static List<ItemStack> getAllItems() {
		List<ItemStack> items = new ArrayList<ItemStack>();
		for (Item i : Item.REGISTRY) {
			items.add(CraftItemStack.asNewCraftStack(i));
		}
		return items;
	}

	public static String getLocalizedName(ItemStack item) {
		return CraftItemStack.asNMSCopy(item).a();
	}

}
