package net.craftersland.customenderchest.transform.serialize;

import net.craftersland.customenderchest.transform.DataTransformation;
import org.bukkit.inventory.ItemStack;

/**
 * ItemStack serializer that uses Papers API addition. It can probably even encode modded items properly.
 */
public class PaperItemSerializer implements DataTransformation<ItemStack, byte[]> {

    @Override
    public byte[] transform(ItemStack element) {
        // TODO: Handle null values, either with exception or with null-serialization
        return element.serializeAsBytes();
    }

    @Override
    public ItemStack transformBack(byte[] element) {
        // TODO: Investigate what exception is thrown on invalid data and wrap it inside a DataTransformationException
        return ItemStack.deserializeBytes(element);
    }
}
