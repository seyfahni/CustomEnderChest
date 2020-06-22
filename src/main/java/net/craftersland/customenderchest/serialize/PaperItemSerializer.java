package net.craftersland.customenderchest.serialize;

import org.bukkit.inventory.ItemStack;

/**
 * ItemStack serializer that uses Papers API addition. It can probably even encode modded items properly.
 */
public class PaperItemSerializer implements ItemSerializer {

    @Override
    public byte[] serializeItem(ItemStack itemStackObject) {
        // TODO: Handle null values, either with exception or with null-serialization
        return itemStackObject.serializeAsBytes();
    }

    @Override
    public ItemStack deserializeItem(byte[] itemStackBinary) {
        // TODO: Investigate what exception is thrown on invalid data and wrap it inside a SerializationException
        return ItemStack.deserializeBytes(itemStackBinary);
    }
}
