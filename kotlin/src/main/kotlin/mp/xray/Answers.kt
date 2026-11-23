package mp.xray

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
 * Inside a Kotlin string, a dollar sign must be written as \$ .
 */
object Answers {
    /* ---------- Part 3 ---------- */

    /**
     * Q1. Look at the bytecode for Empty. Every constructor shows up as a method
     * with a special name in angle brackets. What is that name, including the brackets?
     */
    const val CONSTRUCTOR_NAME = "?"

    /** Q2. Empty.kt is just "class Empty". How many constructors does Empty.class have? */
    const val EMPTY_CONSTRUCTORS = -1

    /**
     * Q3. secondsPerDay() returns SECONDS_PER_DAY, which is 60 * 60 * 24.
     * Look at the bytecode of secondsPerDay(). Is there any multiplication?
     * What number appears in it?
     */
    const val NUMBER_IN_SECONDS_PER_DAY = -1

    /**
     * Q4. greet calls shout. Instructions that call methods all start with
     * "invoke". Which one appears on the line that mentions shout?
     */
    const val STATIC_CALL_INSTRUCTION = "?"

    /** Q5. makeTask returns a lambda. Which "invoke" instruction creates it? */
    const val LAMBDA_INSTRUCTION = "?"

    /**
     * Q6. Outer.kt contains a class Inner inside class Outer. The compiler
     * writes a separate .class file for Inner. What is that file called,
     * without ".class" at the end? (Look at the class name Xray prints, or in
     * the build folder.)
     */
    const val INNER_CLASS_FILE = "?"

    /**
     * Q7. Is this true or false?
     *     ArrayList<String>().javaClass == ArrayList<Int>().javaClass
     * Try it in a main function if you are not sure. Then think about what it
     * means for the <String> and <Int> parts at runtime.
     */
    val GENERICS_SAME_CLASS: Boolean? = null

    /** Q8. Point.kt is one line long. How many methods are declared in Point.class? */
    const val POINT_METHOD_COUNT = -1

    /**
     * Q9. Greeter.kt has functions but no class. The JVM can only run code
     * that lives inside a class, so the compiler made one up. What is its
     * simple name (without the "mp.xray.fixtures." part)?
     * Hint: look at the class names Xray prints.
     */
    const val GREETER_CLASS = "?"

    /**
     * Q10. Look at the bytecode of shout. Before it does anything with text,
     * it calls a method from kotlin/jvm/internal/Intrinsics that you never
     * wrote. What is that method's name? (Think about what it is checking,
     * and why Kotlin bothers.)
     */
    const val HIDDEN_CHECK = "?"

    /* ---------- Part 4 ---------- */

    /**
     * Open loading/Scenario.kt and loading/Rocket.kt.
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
    val PREDICTED_LOG = listOf(
        "start",
    )
}
