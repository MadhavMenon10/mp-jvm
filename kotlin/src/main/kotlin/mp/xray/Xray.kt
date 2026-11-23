package mp.xray

import java.io.PrintWriter
import java.io.StringWriter
import java.util.spi.ToolProvider

/*
 * Provided. Run this to look inside a compiled class.
 * It prints the class file header (using YOUR Part 1 code) and then the
 * disassembled bytecode, exactly what `javap -c -p` prints in a terminal.
 *
 * Run with no arguments to X-ray every fixture, or pass a class name:
 *     mp.xray.fixtures.GreeterKt
 */
object Xray {
    private val defaultTargets = listOf(
        "mp.xray.fixtures.Empty",
        "mp.xray.fixtures.Point",
        "mp.xray.fixtures.GreeterKt",
        "mp.xray.fixtures.Outer\$Inner",
    )

    fun disassemble(target: Class<*>): String {
        val javap = ToolProvider.findFirst("javap")
            .orElseThrow { IllegalStateException("javap not found: run on a full JDK") }
        val path = System.getProperty("java.class.path")
        val out = StringWriter()
        val writer = PrintWriter(out)
        javap.run(writer, writer, "-c", "-p", "-cp", path, target.name)
        writer.flush()
        return out.toString()
    }

    fun xray(target: Class<*>) {
        println("==================== ${target.name}")
        try {
            val bytes = ClassFiles.bytesOf(target)
            val major = ClassFiles.majorVersion(bytes)
            println("magic:         ${ClassFiles.magic(bytes)}")
            println("major version: $major (Java ${ClassFiles.javaVersion(major)})")
            println("file size:     ${bytes.size} bytes")
        } catch (e: NotImplementedError) {
            println("(finish Part 1 to see the class file header)")
        }
        println()
        println(disassemble(target))
    }

    @JvmStatic
    fun main(args: Array<String>) {
        var targets = defaultTargets
        if (args.isNotEmpty()) {
            targets = args.toList()
        }
        for (name in targets) {
            xray(Class.forName(name))
        }
    }
}
