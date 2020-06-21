package net.craftersland.customenderchest.utils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for {@link UUID} related methods.
 */
public final class UuidUtil {

    /**
     * RegEx pattern to match valid or shortened UUIDs. To check if the matched UUID is shortened or not compare the
     * matched string length with 36 or just copy and modify the regex.
     */
    public static final Pattern UUID_PATTERN = Pattern.compile("([0-9a-f]{8})-?([0-9a-f]{4})-?([0-9a-f]{4})-?([0-9a-f]{4})-?([0-9a-f]{12})", Pattern.CASE_INSENSITIVE);

    /**
     * Convert a UUID into a byte array. The most significant byte is stored inside the array's first element, the least
     * significant byte inside the array's last element.
     *
     * @param id the UUID to convert
     * @return a byte array containing the UUID's data
     */
    public static byte[] toBytes(UUID id) {
        byte[] result = new byte[16];
        long lsb = id.getLeastSignificantBits();
        for (int i = 15; i >= 8; i--) {
            result[i] = (byte) (lsb & 0xffL);
            lsb >>= 8;
        }
        long msb = id.getMostSignificantBits();
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (msb & 0xffL);
            msb >>= 8;
        }
        return result;
    }

    /**
     * Convert a valid byte array to into a UUID. The array has to be exactly 16 elements long, otherwise an exception
     * is thrown. The most significant byte has to be stored inside the arrays first element, the least significant byte
     * inside the array's last element.
     *
     * @param bytes the byte array containing the UUID's
     * @return the UUID represented by the byte array
     * @throws IllegalArgumentException when the byte array is not 16 elements long
     */
    public static UUID fromBytes(byte[] bytes) {
        if (bytes.length != 16) {
            throw new IllegalArgumentException("byte array's length is not 16: " + bytes.length);
        }
        long msb = 0;
        for (int i = 0; i < 8; i++) {
            msb <<= 8L;
            msb |= Byte.toUnsignedLong(bytes[i]);
        }
        long lsb = 0;
        for (int i = 8; i < 16; i++) {
            lsb <<= 8L;
            lsb |= Byte.toUnsignedLong(bytes[i]);;
        }
        return new UUID(msb, lsb);
    }

    /**
     * Convert a UUID to its shortest string representation. This will just exclude the dashes and not touch the number
     * representation. The resulting string is 32 characters long. This representation is not part of the UUID standard!
     *
     * @param id the UUID to convert
     * @return 32-character representation of the UUID
     */
    public static String toShortString(UUID id) {
        String longString = id.toString();
        return longString.substring( 0,  8)
                + longString.substring( 9, 13)
                + longString.substring(14, 18)
                + longString.substring(19, 23)
                + longString.substring(24, 36);
    }

    /**
     * Parse a string into a UUID. This method accepts the short as well as the long UUID representation. Any invalid
     * string will result in null being returned. The string will not be trimmed or otherwise touched, do so before
     * passing it as argument if required.
     *
     * @param string the string representation of a UUID
     * @return the parsed UUID or null if invalid
     */
    public static UUID fromString(String string) {
        Matcher uuidMatcher = UuidUtil.UUID_PATTERN.matcher(string);
        if (uuidMatcher.matches()) {
            return UUID.fromString(uuidMatcher.group(1) + "-" + uuidMatcher.group(2) + "-" + uuidMatcher.group(3) + "-" + uuidMatcher.group(4) + "-" + uuidMatcher.group(5));
        } else {
            return null;
        }
    }

    private UuidUtil() {}
}
