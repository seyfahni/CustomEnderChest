package net.craftersland.customenderchest.serialize;

import org.bukkit.inventory.ItemStack;

public class PriorityItemSerializerDecorator implements ItemSerializer {

    ItemSerializer[] serializerPriorityList;

    public PriorityItemSerializerDecorator(ItemSerializer... serializerPriorityList) {
        this.serializerPriorityList = serializerPriorityList;
    }

    @Override
    public byte[] serializeItem(ItemStack itemStackObject) throws SerializationException {
        SerializationException parentException = new SerializationException("no serializer worked");
        for (ItemSerializer itemSerializer : serializerPriorityList) {
            try {
                return itemSerializer.serializeItem(itemStackObject);
            } catch (SerializationException serializationException) {
                parentException.addSuppressed(serializationException);
            }
        }
        throw parentException;
    }

    @Override
    public ItemStack deserializeItem(byte[] itemStackBinary) throws SerializationException {
        SerializationException parentException = new SerializationException("no deserializer matched");
        for (ItemSerializer itemSerializer : serializerPriorityList) {
            try {
                return itemSerializer.deserializeItem(itemStackBinary);
            } catch (SerializationException serializationException) {
                parentException.addSuppressed(serializationException);
            }
        }
        throw parentException;
    }
}
