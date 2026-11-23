package mp.xray

/*
 * =====================================================================
 * Part 2: What did the compiler write?
 * =====================================================================
 *
 * While your program is running, it can ask the JVM questions about any
 * class: "What methods do you have? What constructors?" This is called
 * REFLECTION, because the program is looking at itself.
 *
 * Everything you need starts from a JVM Class object. You get one by
 * writing the class name followed by ::class.java, for example Point::class.java.
 *
 * Useful members (look them up in IntelliJ by typing a dot after the object):
 *
 *     target.declaredMethods        an array of Method objects, one per method
 *     target.declaredConstructors   an array of Constructor objects
 *     method.name                   the method's name, like "toString"
 *     method.isSynthetic            true if the COMPILER made this method up
 *
 * "Declared" means "written in (or generated for) this class", not
 * inherited from a parent class.
 */
object Inspector {
    /**
     * TODO Part 2.1: methodNames
     *
     * Return the name of every method declared in target, sorted
     * alphabetically.
     *
     * How to do it:
     *   1. Make a mutableListOf<String>().
     *   2. Loop over target.declaredMethods.
     *   3. Add each method's name to the list.
     *   4. Return the list sorted (list.sorted() gives a new, sorted list).
     *
     * Example: methodNames(Empty::class.java) gives an empty list.
     * Once it works, try it on Point::class.java. Point.kt is one line long, so
     * where did all those functions come from?
     */
    fun methodNames(target: Class<*>): List<String> {
        TODO("Part 2.1: methodNames")
    }

    /**
     * TODO Part 2.2: syntheticMethodNames
     *
     * Same as methodNames, but only keep methods where isSynthetic is true.
     * A synthetic method is one the compiler invented and marked as
     * "not for humans". You cannot find it anywhere in the .kt file.
     *
     * Example: Point is a data class. The compiler adds a helper for copy()
     * that only it uses, so syntheticMethodNames(Point::class.java) gives a
     * list with one name in it. Run it and see what the name is.
     */
    fun syntheticMethodNames(target: Class<*>): List<String> {
        TODO("Part 2.2: syntheticMethodNames")
    }

    /**
     * TODO Part 2.3: constructorCount
     *
     * Return how many constructors target declares.
     * Hint: an array has a .size.
     *
     * Example: Empty.kt is just "class Empty". Try
     * constructorCount(Empty::class.java) and think about why the answer is not 0.
     */
    fun constructorCount(target: Class<*>): Int {
        TODO("Part 2.3: constructorCount")
    }
}
