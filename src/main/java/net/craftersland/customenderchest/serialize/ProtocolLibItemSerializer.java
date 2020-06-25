package net.craftersland.customenderchest.serialize;

import com.comphenix.protocol.utility.StreamSerializer;
import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;

/**
 * ItemStack serializer using ProtocolLibs API to properly serialize all possible, even modded, ItemStacks. Requires
 * ProtocolLib plugin to be installed.
 */
public class ProtocolLibItemSerializer implements ItemSerializer {

    private final StreamSerializer streamSerializer;

    public ProtocolLibItemSerializer() {
        this(StreamSerializer.getDefault());
    }

    public ProtocolLibItemSerializer(StreamSerializer streamSerializer) {
        this.streamSerializer = streamSerializer;
    }

    @Override
    public byte[] serializeItem(ItemStack itemStackObject) throws SerializationException {
        try (ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
             DataOutputStream outputStream = new DataOutputStream(dataStream)) {
            streamSerializer.serializeItemStack(outputStream, itemStackObject);
            return dataStream.toByteArray();
        } catch (IOException ioException) {
            throw new SerializationException("could not serialize item", ioException);
        }
    }

    @Override
    public ItemStack deserializeItem(byte[] itemStackBinary) throws SerializationException {
        try {
            // hack: library uses byte[] internally, but does not expose decent interface, loop it through base64
            return streamSerializer.deserializeItemStack(Base64Coder.encodeLines(itemStackBinary));
        } catch (IOException ioException) {
            throw new SerializationException("could not deserialize item", ioException);
        }
    }
}
