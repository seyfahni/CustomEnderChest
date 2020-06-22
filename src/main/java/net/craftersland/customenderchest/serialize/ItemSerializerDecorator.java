package net.craftersland.customenderchest.serialize;

import org.bukkit.inventory.ItemStack;

public abstract class ItemSerializerDecorator implements ItemSerializer {

    private final ItemSerializer decorated;

    public ItemSerializerDecorator(ItemSerializer decorated) {
        this.decorated = decorated;
    }

    protected final ItemSerializer getDecorated() {
        return decorated;
    }

    @Override
    public byte[] serializeItem(ItemStack itemStackObject) throws SerializationException {
        return decorated.serializeItem(itemStackObject);
    }

    @Override
    public ItemStack deserializeItem(byte[] itemStackBinary) throws SerializationException {
        return decorated.deserializeItem(itemStackBinary);
    }
}
