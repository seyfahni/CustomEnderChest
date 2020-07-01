package net.craftersland.customenderchest.transform;

import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

/**
 * Encode data as base64.
 */
public class Base64DataTransformer implements DataTransformation<byte[], String> {

    @Override
    public String transform(byte[] binary) throws DataTransformationException {
        return Base64Coder.encodeLines(binary);
    }

    @Override
    public byte[] transformBack(String base64) throws DataTransformationException {
        return Base64Coder.decodeLines(base64);
    }
}
