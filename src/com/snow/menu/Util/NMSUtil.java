package com.snow.menu.Util;

import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.NBTTagList;
import net.minecraft.server.v1_13_R2.RegistryMaterials;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class NMSUtil {


	public static ItemStack setGlowing(ItemStack item, boolean glowing){
		net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
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

	public static List<ItemStack> getAllItems() {
		return RegistryMaterials.ITEM.f()
				.map(CraftItemStack::asNewCraftStack)
				.collect(Collectors.toList());
	}

	/*public static ItemStack forceAmount(ItemStack item, int amount) {
		net.minecraft.server.v1_13_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
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

	public static String getLocalizedName(ItemStack item) {
		return CraftItemStack.asNMSCopy(item).a();
	}*/

}
