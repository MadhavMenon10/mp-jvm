package mp.xray;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;
import java.util.List;
import mp.xray.fixtures.Empty;
import mp.xray.fixtures.Greeter;
import mp.xray.fixtures.Point;
import mp.xray.loading.Scenario;
import org.junit.Test;

public class XrayTest {
    /*
     * Part 3 answers are stored as SHA-256 hashes so this file does not give them away.
     * A hash is a one-way fingerprint: easy to compute from your answer, impossible to reverse.
     */
    private static void assertAnswer(String expectedHash, Object answer) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("SHA-256");
        String hash = HexFormat.of().formatHex(sha.digest(String.valueOf(answer).getBytes(StandardCharsets.UTF_8)));
        assertEquals("Not quite. Look at the Xray output again. Your answer was: " + answer, expectedHash, hash);
    }

    private static byte[] header(int major) {
        return new byte[] {(byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE, 0, 0, 0, (byte) major};
    }

    /* ---------- Part 1 ---------- */

    @Test
    public void part1_magicOnFakeHeader() {
        assertEquals("CAFEBABE", ClassFiles.magic(header(52)));
    }

    @Test
    public void part1_magicOnRealClass() throws Exception {
        assertEquals("CAFEBABE", ClassFiles.magic(ClassFiles.bytesOf(Greeter.class)));
    }

    @Test
    public void part1_majorVersion() {
        assertEquals(52, ClassFiles.majorVersion(header(52)));
        assertEquals(65, ClassFiles.majorVersion(header(65)));
        byte[] big = header(0);
        big[6] = 1;
        big[7] = 2;
        assertEquals(258, ClassFiles.majorVersion(big));
    }

    @Test
    public void part1_javaVersion() {
        assertEquals(8, ClassFiles.javaVersion(52));
        assertEquals(17, ClassFiles.javaVersion(61));
        assertEquals(21, ClassFiles.javaVersion(65));
    }

    @Test
    public void part1_realClassIsRunnableHere() throws Exception {
        int compiledFor = ClassFiles.javaVersion(ClassFiles.majorVersion(ClassFiles.bytesOf(Point.class)));
        assertTrue(compiledFor >= 8);
        assertTrue(compiledFor <= Runtime.version().feature());
    }

    /* ---------- Part 2 ---------- */

    @Test
    public void part2_methodNamesOfGreeter() {
        List<String> names = Inspector.methodNames(Greeter.class);
        assertTrue(names.contains("greet"));
        assertTrue(names.contains("shout"));
        assertTrue(names.contains("secondsPerDay"));
        List<String> sorted = new ArrayList<>(names);
        sorted.sort(null);
        assertEquals(sorted, names);
    }

    @Test
    public void part2_recordGetsGeneratedMethods() {
        assertEquals(List.of("equals", "hashCode", "toString", "x", "y"), Inspector.methodNames(Point.class));
    }

    @Test
    public void part2_lambdaBecomesSyntheticMethod() {
        List<String> synthetic = Inspector.syntheticMethodNames(Greeter.class);
        assertEquals(1, synthetic.size());
        assertTrue(synthetic.get(0).startsWith("lambda$makeTask$"));
        assertEquals(List.of(), Inspector.syntheticMethodNames(Empty.class));
    }

    @Test
    public void part2_constructorCount() {
        assertEquals(1, Inspector.constructorCount(Empty.class));
        assertEquals(1, Inspector.constructorCount(Point.class));
    }

    /* ---------- Part 3 ---------- */

    @Test
    public void part3_q1() throws Exception {
        assertAnswer("abab9a97529ef40561d1363e4b406f76f6831d9dc1f706f1971b38ecae533ffa", Answers.CONSTRUCTOR_NAME);
    }

    @Test
    public void part3_q2() {
        assertEquals(Inspector.constructorCount(Empty.class), Answers.EMPTY_CONSTRUCTORS);
    }

    @Test
    public void part3_q3() throws Exception {
        assertAnswer("b045dd9c18caa8be1e17a4b902152cc1fd292b242b7b9dd74ba8c40896239424", Answers.NUMBER_IN_SECONDS_PER_DAY);
    }

    @Test
    public void part3_q4() throws Exception {
        assertAnswer("715a25f3f69d1c9fc105e1400f0557cc2721a9b98db0208084d26d9526e8d81f", Answers.STATIC_CALL_INSTRUCTION);
    }

    @Test
    public void part3_q5() throws Exception {
        assertAnswer("2655e1fc2a0e712e22a736a130d5de3fec6c10e3bd5d728d88bec983918f0f11", Answers.LAMBDA_INSTRUCTION);
    }

    @Test
    public void part3_q6() throws Exception {
        assertAnswer("95b991b566b61950118bc9c58a6eecdf5a7f8039124b68adb23aebf6efb90f93", Answers.INNER_CLASS_FILE);
    }

    @Test
    public void part3_q7() {
        assertEquals(new ArrayList<String>().getClass() == new ArrayList<Integer>().getClass(), Answers.GENERICS_SAME_CLASS);
    }

    @Test
    public void part3_q8() {
        assertEquals(Point.class.getDeclaredMethods().length, Answers.POINT_METHOD_COUNT);
    }

    @Test
    public void part3_q9() {
        assertEquals(Point.class.getSuperclass().getName(), Answers.POINT_SUPERCLASS);
    }

    @Test
    public void part3_q10() throws Exception {
        assertAnswer("860dd8750986e0c10a0fa5ca166f5a0affb9d945cb0d2cfc9e3b5f2a11ea0c43", Answers.CONCAT_BOOTSTRAP);
    }

    /* ---------- Part 4 ---------- */

    @Test
    public void part4_predictedLogMatchesTheJvm() {
        assertEquals(Scenario.run(), Answers.PREDICTED_LOG);
    }

    /* ---------- Part 5 ---------- */

    private static String launchSequence() {
        return StackLab.whoCalledMe();
    }

    @Test
    public void part5_whoCalledMe() {
        assertEquals("launchSequence", launchSequence());
        assertEquals("part5_whoCalledMe", StackLab.whoCalledMe());
    }

    @Test
    public void part5_recursionDepth() {
        int first = StackLab.maxRecursionDepth();
        int second = StackLab.maxRecursionDepth();
        assertTrue(first > 1000);
        assertTrue(second > 1000);
    }

    @Test
    public void part5_biggerStackGoesDeeper() throws Exception {
        int small = StackLab.depthWithStackSize(256L * 1024);
        int large = StackLab.depthWithStackSize(8L * 1024 * 1024);
        assertTrue(small > 0);
        assertTrue(large > 2 * small);
    }
}
