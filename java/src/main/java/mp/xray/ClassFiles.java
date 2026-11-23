package mp.xray;

import java.io.IOException;
import java.io.InputStream;

/*
 * =====================================================================
 * Part 1: Hello, CAFEBABE
 * =====================================================================
 *
 * When you compile Greeter.java, the compiler writes a file called
 * Greeter.class. That file is not text. It is a list of bytes, and each
 * byte is a number from 0 to 255.
 *
 * Every class file starts with the same 8 bytes of "header":
 *
 *     position:   0    1    2    3    4    5    6    7
 *     meaning:   [ magic number ]  [minor ]  [major ]
 *
 *   - Bytes 0 to 3 are the "magic number". It is the same in every class
 *     file, and it lets the JVM check that a file really is a class file.
 *   - Bytes 6 and 7 are the "major version". It says which version of
 *     Java the file was compiled for.
 *
 * You will write three small methods. Each one is only a few lines.
 * Run the Part 1 tests after each one.
 */
public final class ClassFiles {
    private ClassFiles() {
    }

    /**
     * PROVIDED. You do not need to change this.
     *
     * Gives you the raw bytes of the .class file for any class.
     * Example: ClassFiles.bytesOf(Greeter.class) returns the bytes of Greeter.class.
     */
    public static byte[] bytesOf(Class<?> target) throws IOException {
        String name = target.getName();
        String fileName = name.substring(name.lastIndexOf('.') + 1) + ".class";
        try (InputStream in = target.getResourceAsStream(fileName)) {
            if (in == null) {
                throw new IOException("could not find " + fileName);
            }
            return in.readAllBytes();
        }
    }

    /**
     * PROVIDED. You do not need to change this, but you will call it.
     *
     * In Java, a byte holds a number from -128 to 127, not 0 to 255.
     * So the byte we think of as 0xCA (202) is stored as -54.
     * unsigned(b) undoes that and gives you the 0 to 255 value.
     *
     * Always use unsigned(classFile[i]) instead of classFile[i] in this file.
     */
    public static int unsigned(byte b) {
        return b & 0xFF;
    }

    /**
     * TODO Part 1.1: magic
     *
     * Return the first four bytes of the file as one hex string,
     * using uppercase letters and no spaces.
     *
     * Example: if the first four bytes are 202, 254, 186, 190,
     * return "CAFEBABE" (202 is CA in hex, 254 is FE, and so on).
     *
     * How to do it:
     *   1. Make an empty String (or a StringBuilder).
     *   2. Loop i from 0 to 3.
     *   3. Turn unsigned(classFile[i]) into two hex digits with
     *      String.format("%02X", value) and add it to your string.
     *   4. Return the string.
     */
    public static String magic(byte[] classFile) {
        throw new UnsupportedOperationException("Part 1.1: magic");
    }

    /**
     * TODO Part 1.2: majorVersion
     *
     * Return the major version, which is stored in bytes 6 and 7.
     *
     * A single byte can only hold 0 to 255, so bigger numbers are split
     * across two bytes. Byte 6 is the "high" part and byte 7 is the "low" part:
     *
     *     majorVersion = high * 256 + low
     *
     * This is like reading the number 65 as "6 tens and 5 ones", except
     * each place is worth 256 instead of 10.
     *
     * Example: if byte 6 is 0 and byte 7 is 65, the answer is 65.
     * Example: if byte 6 is 1 and byte 7 is 2, the answer is 258.
     */
    public static int majorVersion(byte[] classFile) {
        throw new UnsupportedOperationException("Part 1.2: majorVersion");
    }

    /**
     * TODO Part 1.3: javaVersion
     *
     * Turn a major version number into the Java release it belongs to.
     *
     * Here are three real data points:
     *     major 52  means  Java 8
     *     major 61  means  Java 17
     *     major 65  means  Java 21
     *
     * Look at the difference between the two numbers in each row.
     * One line of code is enough.
     */
    public static int javaVersion(int majorVersion) {
        throw new UnsupportedOperationException("Part 1.3: javaVersion");
    }
}
