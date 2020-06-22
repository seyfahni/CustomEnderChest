package net.craftersland.customenderchest.serialize;

import org.bukkit.inventory.ItemStack;

public interface ItemSerializer {

    byte[] serializeItem(ItemStack itemStackObject) throws SerializationException;

    ItemStack deserializeItem(byte[] itemStackBinary) throws SerializationException;
}
