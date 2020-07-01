package net.craftersland.customenderchest.transform.serialize;

import com.comphenix.protocol.utility.StreamSerializer;
import net.craftersland.customenderchest.transform.DataTransformation;
import net.craftersland.customenderchest.transform.DataTransformationException;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;

/**
 * ItemStack serializer using ProtocolLibs API to properly serialize all possible, even modded, ItemStacks. Requires
 * ProtocolLib plugin to be installed.
 */
public class ProtocolLibItemSerializer implements DataTransformation<ItemStack, byte[]> {

    private final StreamSerializer streamSerializer;

    public ProtocolLibItemSerializer() {
        this(StreamSerializer.getDefault());
    }

    public ProtocolLibItemSerializer(StreamSerializer streamSerializer) {
        this.streamSerializer = streamSerializer;
    }

    @Override
    public byte[] transform(ItemStack element) throws DataTransformationException {
        try (ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
             DataOutputStream outputStream = new DataOutputStream(dataStream)) {
            streamSerializer.serializeItemStack(outputStream, element);
            return dataStream.toByteArray();
        } catch (IOException ioException) {
            throw new DataTransformationException("could not serialize item", ioException);
        }
    }

    @Override
    public ItemStack transformBack(byte[] element) throws DataTransformationException {
        try {
            // hack: library uses byte[] internally, but does not expose decent interface, loop it through base64
            return streamSerializer.deserializeItemStack(Base64Coder.encodeLines(element));
        } catch (IOException ioException) {
            throw new DataTransformationException("could not deserialize item", ioException);
        }
    }
}
