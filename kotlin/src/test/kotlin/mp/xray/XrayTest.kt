package mp.xray

import mp.xray.fixtures.Empty
import mp.xray.fixtures.Point
import mp.xray.loading.Scenario
import java.security.MessageDigest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class XrayTest {
    private val greeterKt = Class.forName("mp.xray.fixtures.GreeterKt")

    /*
     * Part 3 answers are stored as SHA-256 hashes so this file does not give them away.
     * A hash is a one-way fingerprint: easy to compute from your answer, impossible to reverse.
     */
    private fun assertAnswer(expectedHash: String, answer: Any?) {
        val digest = MessageDigest.getInstance("SHA-256").digest(answer.toString().toByteArray())
        val hash = digest.joinToString("") { "%02x".format(it) }
        assertEquals("Not quite. Look at the Xray output again. Your answer was: $answer", expectedHash, hash)
    }

    private fun header(major: Int): ByteArray {
        return byteArrayOf(0xCA.toByte(), 0xFE.toByte(), 0xBA.toByte(), 0xBE.toByte(), 0, 0, 0, major.toByte())
    }

    /* ---------- Part 1 ---------- */

    @Test
    fun part1_magicOnFakeHeader() {
        assertEquals("CAFEBABE", ClassFiles.magic(header(52)))
    }

    @Test
    fun part1_magicOnRealClass() {
        assertEquals("CAFEBABE", ClassFiles.magic(ClassFiles.bytesOf(greeterKt)))
    }

    @Test
    fun part1_majorVersion() {
        assertEquals(52, ClassFiles.majorVersion(header(52)))
        assertEquals(65, ClassFiles.majorVersion(header(65)))
        val big = header(0)
        big[6] = 1
        big[7] = 2
        assertEquals(258, ClassFiles.majorVersion(big))
    }

    @Test
    fun part1_javaVersion() {
        assertEquals(8, ClassFiles.javaVersion(52))
        assertEquals(17, ClassFiles.javaVersion(61))
        assertEquals(21, ClassFiles.javaVersion(65))
    }

    @Test
    fun part1_realClassIsRunnableHere() {
        val compiledFor = ClassFiles.javaVersion(ClassFiles.majorVersion(ClassFiles.bytesOf(Point::class.java)))
        assertTrue(compiledFor >= 8)
        assertTrue(compiledFor <= Runtime.version().feature())
    }

    /* ---------- Part 2 ---------- */

    @Test
    fun part2_methodNamesOfGreeterKt() {
        val names = Inspector.methodNames(greeterKt)
        assertTrue(names.contains("greet"))
        assertTrue(names.contains("shout"))
        assertTrue(names.contains("secondsPerDay"))
        assertEquals(names.sorted(), names)
    }

    @Test
    fun part2_dataClassGetsGeneratedMethods() {
        val expected = listOf(
            "component1", "component2", "copy", "copy\$default", "equals",
            "getX", "getY", "hashCode", "toString",
        )
        assertEquals(expected, Inspector.methodNames(Point::class.java))
    }

    @Test
    fun part2_syntheticMethods() {
        assertEquals(listOf("copy\$default"), Inspector.syntheticMethodNames(Point::class.java))
        assertEquals(listOf<String>(), Inspector.syntheticMethodNames(Empty::class.java))
    }

    @Test
    fun part2_constructorCount() {
        assertEquals(1, Inspector.constructorCount(Empty::class.java))
        assertEquals(1, Inspector.constructorCount(Point::class.java))
        assertEquals(0, Inspector.constructorCount(greeterKt))
    }

    /* ---------- Part 3 ---------- */

    @Test
    fun part3_q1() {
        assertAnswer("abab9a97529ef40561d1363e4b406f76f6831d9dc1f706f1971b38ecae533ffa", Answers.CONSTRUCTOR_NAME)
    }

    @Test
    fun part3_q2() {
        assertEquals(Inspector.constructorCount(Empty::class.java), Answers.EMPTY_CONSTRUCTORS)
    }

    @Test
    fun part3_q3() {
        assertAnswer("b045dd9c18caa8be1e17a4b902152cc1fd292b242b7b9dd74ba8c40896239424", Answers.NUMBER_IN_SECONDS_PER_DAY)
    }

    @Test
    fun part3_q4() {
        assertAnswer("715a25f3f69d1c9fc105e1400f0557cc2721a9b98db0208084d26d9526e8d81f", Answers.STATIC_CALL_INSTRUCTION)
    }

    @Test
    fun part3_q5() {
        assertAnswer("2655e1fc2a0e712e22a736a130d5de3fec6c10e3bd5d728d88bec983918f0f11", Answers.LAMBDA_INSTRUCTION)
    }

    @Test
    fun part3_q6() {
        assertAnswer("95b991b566b61950118bc9c58a6eecdf5a7f8039124b68adb23aebf6efb90f93", Answers.INNER_CLASS_FILE)
    }

    @Test
    fun part3_q7() {
        assertEquals(ArrayList<String>().javaClass == ArrayList<Int>().javaClass, Answers.GENERICS_SAME_CLASS)
    }

    @Test
    fun part3_q8() {
        assertEquals(Point::class.java.declaredMethods.size, Answers.POINT_METHOD_COUNT)
    }

    @Test
    fun part3_q9() {
        assertEquals(greeterKt.simpleName, Answers.GREETER_CLASS)
    }

    @Test
    fun part3_q10() {
        assertAnswer("08d9b44f7343358be79791dbf8835156d870d12d9e21ad969796ae9b6c0cf7c2", Answers.HIDDEN_CHECK)
    }

    /* ---------- Part 4 ---------- */

    @Test
    fun part4_predictedLogMatchesTheJvm() {
        assertEquals(Scenario.run(), Answers.PREDICTED_LOG)
    }

    /* ---------- Part 5 ---------- */

    private fun launchSequence(): String {
        return StackLab.whoCalledMe()
    }

    @Test
    fun part5_whoCalledMe() {
        assertEquals("launchSequence", launchSequence())
        assertEquals("part5_whoCalledMe", StackLab.whoCalledMe())
    }

    @Test
    fun part5_recursionDepth() {
        val first = StackLab.maxRecursionDepth()
        val second = StackLab.maxRecursionDepth()
        assertTrue(first > 1000)
        assertTrue(second > 1000)
    }

    @Test
    fun part5_biggerStackGoesDeeper() {
        val small = StackLab.depthWithStackSize(256L * 1024)
        val large = StackLab.depthWithStackSize(8L * 1024 * 1024)
        assertTrue(small > 0)
        assertTrue(large > 2 * small)
    }
}
