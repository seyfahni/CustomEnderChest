package net.craftersland.customenderchest.serialize;

import org.bukkit.inventory.ItemStack;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Compress data with gzip.
 */
public class GzipItemSerializerDecorator extends ItemSerializerDecorator {

    public GzipItemSerializerDecorator(ItemSerializer decorated) {
        super(decorated);
    }

    @Override
    public byte[] serializeItem(ItemStack itemStackObject) throws SerializationException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
             GZIPOutputStream dataOutput = new GZIPOutputStream(outputStream)) {
            byte[] uncompressed = super.serializeItem(itemStackObject);
            dataOutput.write(uncompressed);
            return outputStream.toByteArray();
        } catch (IOException ioException) {
            throw new SerializationException("could not gzip compress item", ioException);
        }
    }

    @Override
    public ItemStack deserializeItem(byte[] itemStackBinary) throws SerializationException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(itemStackBinary);
             GZIPInputStream dataInput = new GZIPInputStream(inputStream)) {
            byte[] uncompressed = dataInput.readAllBytes();
            return super.deserializeItem(uncompressed);
        } catch (IOException exception) {
            throw new SerializationException("could not gzip uncompress item", exception);
        }
    }
}
