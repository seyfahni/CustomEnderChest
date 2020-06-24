package net.craftersland.customenderchest.serialize;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GzipItemSerializerDecoratorTest {

    @Test
    public void testCompressionCycle() throws SerializationException {
        ItemStack testItem = new ItemStack(Material.BOOK, 23);
        byte[] testBinary = "This is a test-serialized item.".getBytes(StandardCharsets.UTF_8);

        ItemSerializer mockedSerializer = mock(ItemSerializer.class);
        when(mockedSerializer.serializeItem(testItem)).thenReturn(testBinary);
        when(mockedSerializer.deserializeItem(testBinary)).thenReturn(testItem);

        GzipItemSerializerDecorator gzipDecorator = new GzipItemSerializerDecorator(mockedSerializer);

        byte[] gzipped = gzipDecorator.serializeItem(testItem);
        assertEquals(testItem, gzipDecorator.deserializeItem(gzipped));
    }

    @Test
    public void testInvalidGzip() {
        byte[] notGzipped = "Well, I doubt that this is valid gzip binary.".getBytes(StandardCharsets.UTF_8);

        ItemSerializer mockedSerializer = mock(ItemSerializer.class);

        GzipItemSerializerDecorator gzipDecorator = new GzipItemSerializerDecorator(mockedSerializer);

        assertThrows(SerializationException.class, () -> gzipDecorator.deserializeItem(notGzipped));
    }
}