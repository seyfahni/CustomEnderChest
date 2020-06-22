package net.craftersland.customenderchest.serialize;

import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Basic ItemSerializer implementation that uses the basic Bukkit API. It should always work but may not encode
 * non-vanilla item data like custom NBT or modded items.
 */
public class BukkitItemSerializer implements ItemSerializer {

    @Override
    public byte[] serializeItem(ItemStack itemStackObject) throws SerializationException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
            dataOutput.writeObject(itemStackObject);
            return outputStream.toByteArray();
        } catch (IOException ioException) {
            throw new SerializationException("could not serialize item", ioException);
        }
    }

    @Override
    public ItemStack deserializeItem(byte[] itemStackBinary) throws SerializationException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(itemStackBinary);
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
            return (ItemStack) dataInput.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException exception) {
            throw new SerializationException("could not deserialize item", exception);
        }
    }
}
