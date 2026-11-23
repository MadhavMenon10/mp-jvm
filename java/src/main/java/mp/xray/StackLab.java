package mp.xray;

/*
 * =====================================================================
 * Part 5: Life on the stack
 * =====================================================================
 *
 * When you call a method, the JVM needs somewhere to keep that method's
 * parameters and local variables. It puts them in a FRAME and pushes the
 * frame on top of the CALL STACK. When the method returns, its frame is
 * popped off.
 *
 *     main() calls a(), a() calls b():
 *
 *         | b's frame |   <- top: the method running right now
 *         | a's frame |
 *         | main's    |   <- bottom
 *
 * The stack has a fixed size, chosen when the thread starts. If you call
 * methods inside methods inside methods without ever returning, the stack
 * fills up and the JVM throws StackOverflowError.
 */
public final class StackLab {
    private static int depth = 0;

    private StackLab() {
    }

    /**
     * TODO Part 5.1: whoCalledMe
     *
     * Return the NAME of the method that called whoCalledMe().
     *
     * Thread.currentThread().getStackTrace() returns an array that shows
     * the current call stack, with the top of the stack at index 0.
     * Each element has a getMethodName() method.
     *
     * Example: if a method called launch() calls StackLab.whoCalledMe(),
     * the answer is "launch".
     *
     * How to do it:
     *   1. Get the array: StackTraceElement[] stack = Thread.currentThread().getStackTrace();
     *   2. FIRST, print every element in a loop and run the Part 5 test once.
     *      Look at index 0, 1 and 2. Which one is the caller?
     *   3. Return that element's getMethodName(), and delete your print loop.
     */
    public static String whoCalledMe() {
        throw new UnsupportedOperationException("Part 5.1: whoCalledMe");
    }

    /**
     * TODO Part 5.2: maxRecursionDepth
     *
     * Find out how many method calls fit on the stack.
     *
     * How to do it:
     *   1. Write a new private static helper method (call it dive) that adds 1
     *      to the depth field and then calls itself. Give it NO base case,
     *      so it never stops on its own.
     *   2. In maxRecursionDepth: set depth to 0 first, so the method works
     *      if it is called more than once.
     *   3. Call dive() inside a try block, and catch StackOverflowError.
     *   4. Return depth.
     *
     * Normally a StackOverflowError is a bug. Here we cause one on purpose
     * to measure the stack. You should get a number in the thousands.
     */
    public static int maxRecursionDepth() {
        throw new UnsupportedOperationException("Part 5.2: maxRecursionDepth");
    }

    /**
     * PROVIDED. You do not need to change this.
     *
     * Starts a brand new thread whose stack is stackBytes big, runs your
     * maxRecursionDepth() on it, and returns the result. This lets you
     * compare a small stack with a big one.
     */
    public static int depthWithStackSize(long stackBytes) throws InterruptedException {
        int[] result = new int[1];
        Thread thread = new Thread(null, () -> {
            result[0] = maxRecursionDepth();
        }, "stack-lab", stackBytes);
        thread.start();
        thread.join();
        return result[0];
    }

    /** Run this once both TODOs work, and compare the three numbers. */
    public static void main(String[] args) throws InterruptedException {
        System.out.println("default thread: " + maxRecursionDepth() + " calls");
        System.out.println("256 KB stack:   " + depthWithStackSize(256L * 1024) + " calls");
        System.out.println("8 MB stack:     " + depthWithStackSize(8L * 1024 * 1024) + " calls");
    }
}
