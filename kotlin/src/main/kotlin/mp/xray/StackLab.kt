package mp.xray

/*
 * =====================================================================
 * Part 5: Life on the stack
 * =====================================================================
 *
 * When you call a function, the JVM needs somewhere to keep that
 * function's parameters and local variables. It puts them in a FRAME and
 * pushes the frame on top of the CALL STACK. When the function returns,
 * its frame is popped off.
 *
 *     main() calls a(), a() calls b():
 *
 *         | b's frame |   <- top: the function running right now
 *         | a's frame |
 *         | main's    |   <- bottom
 *
 * The stack has a fixed size, chosen when the thread starts. If you call
 * functions inside functions inside functions without ever returning, the
 * stack fills up and the JVM throws StackOverflowError.
 */
object StackLab {
    private var depth = 0

    /**
     * TODO Part 5.1: whoCalledMe
     *
     * Return the NAME of the function that called whoCalledMe().
     *
     * Thread.currentThread().stackTrace is an array that shows the current
     * call stack, with the top of the stack at index 0. Each element has a
     * methodName property.
     *
     * Example: if a function called launch() calls StackLab.whoCalledMe(),
     * the answer is "launch".
     *
     * How to do it:
     *   1. Get the array: val stack = Thread.currentThread().stackTrace
     *   2. FIRST, print every element in a loop and run the Part 5 test once.
     *      Look at index 0, 1 and 2. Which one is the caller?
     *   3. Return that element's methodName, and delete your print loop.
     */
    fun whoCalledMe(): String {
        TODO("Part 5.1: whoCalledMe")
    }

    /**
     * TODO Part 5.2: maxRecursionDepth
     *
     * Find out how many function calls fit on the stack.
     *
     * How to do it:
     *   1. Write a new private helper function (call it dive) that adds 1
     *      to depth and then calls itself. Give it NO base case, so it never
     *      stops on its own.
     *   2. In maxRecursionDepth: set depth to 0 first, so the function works
     *      if it is called more than once.
     *   3. Call dive() inside a try block, and catch StackOverflowError.
     *   4. Return depth.
     *
     * Normally a StackOverflowError is a bug. Here we cause one on purpose
     * to measure the stack. You should get a number in the thousands.
     */
    fun maxRecursionDepth(): Int {
        TODO("Part 5.2: maxRecursionDepth")
    }

    /**
     * PROVIDED. You do not need to change this.
     *
     * Starts a brand new thread whose stack is stackBytes big, runs your
     * maxRecursionDepth() on it, and returns the result. This lets you
     * compare a small stack with a big one.
     */
    fun depthWithStackSize(stackBytes: Long): Int {
        var result = 0
        val thread = Thread(null, { result = maxRecursionDepth() }, "stack-lab", stackBytes)
        thread.start()
        thread.join()
        return result
    }

    /** Run this once both TODOs work, and compare the three numbers. */
    @JvmStatic
    fun main(args: Array<String>) {
        println("default thread: ${maxRecursionDepth()} calls")
        println("256 KB stack:   ${depthWithStackSize(256L * 1024)} calls")
        println("8 MB stack:     ${depthWithStackSize(8L * 1024 * 1024)} calls")
    }
}
