package mp.xray;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.spi.ToolProvider;

/*
 * Provided. Run this to look inside a compiled class.
 * It prints the class file header (using YOUR Part 1 code) and then the
 * disassembled bytecode, exactly what `javap -c -p` prints in a terminal.
 *
 * Run with no arguments to X-ray every fixture, or pass a class name:
 *     mp.xray.fixtures.Greeter
 */
public final class Xray {
    private static final String[] DEFAULT_TARGETS = {
        "mp.xray.fixtures.Empty",
        "mp.xray.fixtures.Point",
        "mp.xray.fixtures.Greeter",
        "mp.xray.fixtures.Outer$Inner",
    };

    private Xray() {
    }

    public static String disassemble(Class<?> target) {
        ToolProvider javap = ToolProvider.findFirst("javap")
            .orElseThrow(() -> new IllegalStateException("javap not found: run on a full JDK"));
        String path = System.getProperty("java.class.path");
        StringWriter out = new StringWriter();
        PrintWriter writer = new PrintWriter(out);
        javap.run(writer, writer, "-c", "-p", "-cp", path, target.getName());
        writer.flush();
        return out.toString();
    }

    public static void xray(Class<?> target) throws Exception {
        System.out.println("==================== " + target.getName());
        try {
            byte[] bytes = ClassFiles.bytesOf(target);
            int major = ClassFiles.majorVersion(bytes);
            System.out.println("magic:         " + ClassFiles.magic(bytes));
            System.out.println("major version: " + major + " (Java " + ClassFiles.javaVersion(major) + ")");
            System.out.println("file size:     " + bytes.length + " bytes");
        } catch (UnsupportedOperationException e) {
            System.out.println("(finish Part 1 to see the class file header)");
        }
        System.out.println();
        System.out.println(disassemble(target));
    }

    public static void main(String[] args) throws Exception {
        String[] targets = DEFAULT_TARGETS;
        if (args.length > 0) {
            targets = args;
        }
        for (String name : targets) {
            xray(Class.forName(name));
        }
    }
}
