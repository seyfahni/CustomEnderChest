package net.craftersland.customenderchest.serialize;

import net.craftersland.customenderchest.transform.DataTransformationException;
import net.craftersland.customenderchest.transform.GzipCompressionTransformer;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class GzipCompressionTransformerTest {

    @Test
    public void testCompressionCycle() throws DataTransformationException {
        byte[] testBinary = "This is a test-string that will be gzip compressed and back.".getBytes(StandardCharsets.UTF_8);

        GzipCompressionTransformer gzipDecorator = new GzipCompressionTransformer();

        byte[] gzipped = gzipDecorator.transform(testBinary);
        assertEquals(testBinary, gzipDecorator.transformBack(gzipped));
    }

    @Test
    public void testInvalidGzip() {
        byte[] notGzipped = "Well, I doubt that this is valid gzip binary.".getBytes(StandardCharsets.UTF_8);

        GzipCompressionTransformer gzipDecorator = new GzipCompressionTransformer();

        assertThrows(DataTransformationException.class, () -> gzipDecorator.transformBack(notGzipped));
    }
}