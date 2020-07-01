package net.craftersland.customenderchest.transform;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Compress data with gzip.
 */
public class GzipCompressionTransformer implements DataTransformation<byte[], byte[]> {

    @Override
    public byte[] transform(byte[] uncompressed) throws DataTransformationException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            try (GZIPOutputStream dataOutput = new GZIPOutputStream(outputStream)) {
                dataOutput.write(uncompressed);
            } // when dataOutput is closed it writes additional end-of-file data
            return outputStream.toByteArray();
        } catch (IOException ioException) {
            throw new DataTransformationException("could not gzip compress item", ioException);
        }
    }

    @Override
    public byte[] transformBack(byte[] compressed) throws DataTransformationException {
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(compressed);
             GZIPInputStream dataInput = new GZIPInputStream(inputStream)) {
            return dataInput.readAllBytes();
        } catch (IOException exception) {
            throw new DataTransformationException("could not gzip uncompress item", exception);
        }
    }
}
