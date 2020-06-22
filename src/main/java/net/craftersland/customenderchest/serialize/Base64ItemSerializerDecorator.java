package net.craftersland.customenderchest.serialize;

import org.bukkit.inventory.ItemStack;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.nio.charset.StandardCharsets;

/**
 * Encode data as base64. Although the method still returns a byte array, the content is utf8 encoded base64.
 */
public class Base64ItemSerializerDecorator extends ItemSerializerDecorator {

    public Base64ItemSerializerDecorator(ItemSerializer decorated) {
        super(decorated);
    }

    @Override
    public byte[] serializeItem(ItemStack itemStackObject) throws SerializationException {
        byte[] raw = super.serializeItem(itemStackObject);
        String base64 = Base64Coder.encodeLines(raw);
        return base64.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public ItemStack deserializeItem(byte[] itemStackBinary) throws SerializationException {
        String base64 = new String(itemStackBinary, StandardCharsets.UTF_8);
        byte[] raw = Base64Coder.decodeLines(base64);
        return super.deserializeItem(raw);
    }
}
