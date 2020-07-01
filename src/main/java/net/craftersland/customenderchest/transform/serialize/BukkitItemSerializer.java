package net.craftersland.customenderchest.transform.serialize;

import net.craftersland.customenderchest.transform.DataTransformation;
import net.craftersland.customenderchest.transform.DataTransformationException;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Data transformer implementation that uses the basic Bukkit API to serialize items. It should always work but may not
 * encode non-vanilla item data like custom NBT or modded items.
 */
public class BukkitItemSerializer implements DataTransformation<ItemStack, byte[]> {

    @Override
    public byte[] transform(ItemStack element) throws DataTransformationException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream)) {
            dataOutput.writeObject(element);
            return outputStream.toByteArray();
        } catch (IOException ioException) {
            throw new DataTransformationException("could not serialize item", ioException);
        }
    }

    @Override
    public ItemStack transformBack(byte[] element) throws DataTransformationException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(element);
             BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream)) {
            return (ItemStack) dataInput.readObject();
        } catch (IOException | ClassNotFoundException | ClassCastException exception) {
            throw new DataTransformationException("could not deserialize item", exception);
        }
    }
}
