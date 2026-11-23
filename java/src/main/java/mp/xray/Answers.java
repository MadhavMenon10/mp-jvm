package mp.xray;

import java.util.List;

/*
 * =====================================================================
 * Part 3: Bytecode detective      and      Part 4: Lazy loading
 * =====================================================================
 *
 * For each question, replace the placeholder ("?", -1, or null) with your
 * answer. Do not change the names of the constants.
 *
 * HOW TO FIND THE ANSWERS (Part 3):
 *   1. Run Xray.main(). It prints the bytecode of every class in fixtures/.
 *   2. Find the class the question is about. Each one starts with a line of = signs.
 *   3. Find the method the question is about. Its instructions are listed under "Code:".
 *   4. Read the instructions and the comments after "//". The answer is in there.
 *
 * Every answer can be found in the Xray output or with your Inspector from Part 2.
 * You never need to guess.
 *
 * Strings must match exactly, including uppercase and lowercase letters.
 */
public final class Answers {
    private Answers() {
    }

    /* ---------- Part 3 ---------- */

    /**
     * Q1. Look at the bytecode for Empty. Every constructor shows up as a method
     * with a special name in angle brackets. What is that name, including the brackets?
     */
    public static final String CONSTRUCTOR_NAME = "?";

    /** Q2. Empty.java has nothing inside its braces. How many constructors does Empty.class have? */
    public static final int EMPTY_CONSTRUCTORS = -1;

    /**
     * Q3. Greeter.secondsPerDay() returns SECONDS_PER_DAY, which is 60 * 60 * 24.
     * Look at the bytecode of secondsPerDay(). Is there any multiplication?
     * What number appears in it?
     */
    public static final int NUMBER_IN_SECONDS_PER_DAY = -1;

    /**
     * Q4. Greeter.greet calls Greeter.shout, which is a static method.
     * Instructions that call methods all start with "invoke". Which one
     * appears on the line that mentions shout?
     */
    public static final String STATIC_CALL_INSTRUCTION = "?";

    /** Q5. Greeter.makeTask returns a lambda. Which "invoke" instruction creates it? */
    public static final String LAMBDA_INSTRUCTION = "?";

    /**
     * Q6. Outer.java contains a class Inner inside class Outer. The compiler
     * writes a separate .class file for Inner. What is that file called,
     * without ".class" at the end? (Look at the class name Xray prints, or in
     * the build folder.)
     */
    public static final String INNER_CLASS_FILE = "?";

    /**
     * Q7. Is this true or false?
     *     new ArrayList<String>().getClass() == new ArrayList<Integer>().getClass()
     * Try it in a main method if you are not sure. Then think about what it
     * means for the <String> and <Integer> parts at runtime.
     */
    public static final Boolean GENERICS_SAME_CLASS = null;

    /** Q8. Point.java is one line long. How many methods are declared in Point.class? */
    public static final int POINT_METHOD_COUNT = -1;

    /**
     * Q9. Point never says "extends". But every class has a parent class.
     * What is Point's parent? Give the full name, like "java.lang.Something".
     * Hint: Point.class.getSuperclass()
     */
    public static final String POINT_SUPERCLASS = "?";

    /**
     * Q10. Greeter.greet builds the string "hello " + name. Look for the
     * invokedynamic instruction in greet. The comment after it has the form
     *     InvokeDynamic #0:SOMENAME:(...)
     * What is SOMENAME? (This is the method the JVM uses to glue strings together.)
     */
    public static final String CONCAT_BOOTSTRAP = "?";

    /* ---------- Part 4 ---------- */

    /**
     * Open loading/Scenario.java and loading/Rocket.java.
     *
     * Scenario.run() writes messages to a log. Rocket also writes a message
     * ("Rocket class initialized") the moment the JVM sets up the Rocket class.
     *
     * BEFORE you run anything, write down every message you expect in the log,
     * in order, one String per line. Copy the text exactly. One of them is
     * already filled in for you.
     *
     * Then run Scenario.main() to check. If you were wrong, that's fine: keep
     * your first guess in a comment, fix the list, and explain the surprise
     * in REFLECTION.md.
     */
    public static final List<String> PREDICTED_LOG = List.of(
        "start"
    );
}
