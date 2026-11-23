package mp.xray;

import java.util.List;

/*
 * =====================================================================
 * Part 2: What did the compiler write?
 * =====================================================================
 *
 * While your program is running, it can ask the JVM questions about any
 * class: "What methods do you have? What constructors?" This is called
 * REFLECTION, because the program is looking at itself.
 *
 * Everything you need starts from a Class object. You get one by writing
 * the class name followed by .class, for example Point.class.
 *
 * Useful methods (look them up in IntelliJ by typing a dot after the object):
 *
 *     target.getDeclaredMethods()       an array of Method objects, one per method
 *     target.getDeclaredConstructors()  an array of Constructor objects
 *     method.getName()                  the method's name, like "toString"
 *     method.isSynthetic()              true if the COMPILER made this method up
 *
 * "Declared" means "written in (or generated for) this class", not
 * inherited from a parent class.
 */
public final class Inspector {
    private Inspector() {
    }

    /**
     * TODO Part 2.1: methodNames
     *
     * Return the name of every method declared in target, sorted
     * alphabetically.
     *
     * How to do it:
     *   1. Make a new ArrayList<String>.
     *   2. Loop over target.getDeclaredMethods().
     *   3. Add each method's getName() to the list.
     *   4. Sort the list (list.sort(null) sorts Strings alphabetically).
     *   5. Return the list.
     *
     * You will need: import java.lang.reflect.Method; and import java.util.ArrayList;
     *
     * Example: methodNames(Greeter.class) includes "greet", "secondsPerDay" and "shout".
     * Once it works, try it on Point.class. Point.java is one line long, so where
     * did all those methods come from?
     */
    public static List<String> methodNames(Class<?> target) {
        throw new UnsupportedOperationException("Part 2.1: methodNames");
    }

    /**
     * TODO Part 2.2: syntheticMethodNames
     *
     * Same as methodNames, but only keep methods where isSynthetic() is true.
     * A synthetic method is one the compiler invented. You cannot find it
     * anywhere in the .java file.
     *
     * Example: Greeter has a lambda in makeTask(). The compiler turns the
     * body of that lambda into a hidden method, so syntheticMethodNames(Greeter.class)
     * gives a list with one name in it. Run it and see what the name is.
     */
    public static List<String> syntheticMethodNames(Class<?> target) {
        throw new UnsupportedOperationException("Part 2.2: syntheticMethodNames");
    }

    /**
     * TODO Part 2.3: constructorCount
     *
     * Return how many constructors target declares.
     * Hint: an array has a .length.
     *
     * Example: Empty.java has nothing inside the braces. Try
     * constructorCount(Empty.class) and think about why the answer is not 0.
     */
    public static int constructorCount(Class<?> target) {
        throw new UnsupportedOperationException("Part 2.3: constructorCount");
    }
}
