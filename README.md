# mp_jvm

## Goals and Overview

In this MP you will:

- Read the header of a compiled `.class` file
- Use reflection to look at the methods the compiler generates for you
- Read JVM bytecode with `javap`
- See when the JVM loads and initializes a class
- Measure the size of the call stack
- Watch the JIT compiler speed up a running program

## Background

Your computer can't run Java or Kotlin source code directly. When you press Run, IntelliJ first calls a compiler (`javac` for Java, `kotlinc` for Kotlin), which turns each class into a `.class` file full of **bytecode**. Bytecode is a list of small instructions for the Java Virtual Machine (JVM), which is the program that actually runs your code.

Here is a short method and the bytecode the compiler writes for it:

```
static int square(int x) {      0: iload_0      // push x
    return x * x;               1: iload_0      // push x again
}                               2: imul         // multiply the top two values
                                3: ireturn      // return the result
```

Java and Kotlin both compile to the same kind of `.class` file. Once your code is bytecode, the JVM has no way of knowing which language it came from. Most of this MP is about looking at that bytecode and at what the JVM does with it.

## Checking Out the Code

Clone the repository:

```
git clone https://github.com/MadhavMenon10/mp-jvm.git
```

The repository has two copies of the MP. Use the `java` folder if you are taking the course in Java, and the `kotlin` folder if you are taking it in Kotlin. The parts and points are the same in both. A few of the Part 3 questions are different, because the two compilers make different choices.

## Preparing Your Code

In IntelliJ, open the `java` or `kotlin` folder (not the top folder of the repository) as a Gradle project. You will need JDK 17 or newer.

To run the tests from the terminal:

```
cd java          # or: cd kotlin
./gradlew test   # on Windows: gradlew.bat test
```

You can also run them from IntelliJ by opening `XrayTest` and clicking the arrow next to the class name. All of the tests will fail until you start writing code. Each test name starts with its part (`part1_`, `part2_`, ...), so you can run one part at a time.

The files you will edit are:

- `ClassFiles` (Part 1)
- `Inspector` (Part 2)
- `Answers` (Parts 3 and 4)
- `StackLab` (Part 5)
- `REFLECTION.md` (Part 6)

**Do not modify** anything in the `fixtures` or `loading` folders. The tests depend on those files being exactly as given.

### The Xray tool

`Xray` is a provided program that prints the header and the bytecode of the classes in `fixtures`. Run its `main` method from IntelliJ. You can also pass a class name as a program argument (for example `mp.xray.loading.Scenario`) to look at a single class. Until you finish Part 1, the header will show a message instead of the version number.

`Xray` is a wrapper around a JDK tool called `javap`. If you prefer the terminal, this prints the same output:

```
javap -c -p -cp build/classes/java/main mp.xray.fixtures.Greeter
```

In the Kotlin MP the path is `build/classes/kotlin/main` and the class is `mp.xray.fixtures.GreeterKt`.

## Part 1: Hello, CAFEBABE

A `.class` file is a sequence of bytes. The first eight bytes are the same layout in every class file:

| Bytes | Contents |
|---|---|
| 0 to 3 | The magic number |
| 4 to 5 | The minor version |
| 6 to 7 | The major version |

The magic number is always `CA FE BA BE` in hexadecimal. The JVM checks it before it will load a file. The major version tells you which version of Java the file was compiled for.

You will need to implement three methods in `ClassFiles`.

### magic

`magic` should return the first four bytes as an uppercase hex string with no spaces, like `"CAFEBABE"`. `String.format("%02X", value)` (or `"%02X".format(value)` in Kotlin) turns one byte into two hex digits.

**NOTE:** Bytes in Java and Kotlin are signed, so they range from -128 to 127. The byte `0xCA` (202) is stored as -54. Pass every byte through the provided `unsigned` method before you use it.

### majorVersion

The major version is split across bytes 6 and 7, since a single byte can only hold values up to 255. Byte 6 is the high byte, so the full value is `high * 256 + low`.

### javaVersion

`javaVersion` converts a major version into a Java release number. You are given three data points: 52 is Java 8, 61 is Java 17, and 65 is Java 21. Can you find the pattern?

Once these pass, run `Xray` again. Which version of Java was your code compiled for?

## Part 2: What Did the Compiler Write?

A running program can ask the JVM about any loaded class through **reflection**. In Java you get a class's `Class` object with `Point.class`, and in Kotlin with `Point::class.java`. From there, `getDeclaredMethods()` and `getDeclaredConstructors()` (`declaredMethods` and `declaredConstructors` in Kotlin) give you arrays describing the class.

You will need to implement three methods in `Inspector`:

- `methodNames` returns the names of every declared method, sorted alphabetically.
- `syntheticMethodNames` returns only the methods where `isSynthetic()` is true. A synthetic method is one the compiler created that doesn't appear anywhere in the source.
- `constructorCount` returns the number of declared constructors.

After these pass, try `methodNames` on `Point`. The source for `Point` is a single line. Where did the rest of those methods come from?

## Part 3: Bytecode Detective

`Answers` contains ten questions about the bytecode of the classes in `fixtures`. Replace each placeholder (`"?"`, `-1` or `null`) with your answer. Every answer can be found in the `Xray` output or by using your `Inspector`, so there is no need to guess.

A few tips for reading the output:

- Each method's instructions are listed under `Code:`, one per line.
- The text after `//` is a comment from `javap` that tells you what an instruction refers to. Most of the answers are in these comments.
- Instructions that call methods all start with `invoke`.

String answers are case sensitive.

**NOTE:** The Part 3 tests store a hash of each answer instead of the answer itself, so looking in `XrayTest` won't tell you anything. If a test fails, it will print the answer you gave.

## Part 4: Lazy Loading

The JVM doesn't set up every class when your program starts. It **initializes** a class the first time the class is actually needed, and it only does this once per run.

`loading/Rocket` writes `"Rocket class initialized"` to a log when it is initialized. `loading/Scenario` uses `Rocket` in several different ways and logs a message after each step.

Before you run anything, read `Scenario.run` and write the full log you expect into `Answers.PREDICTED_LOG`, one string per message, in order. Then run `Scenario.main` and compare. If your prediction was wrong, keep your original guess in a comment, fix the list, and explain what surprised you in `REFLECTION.md`.

If one line doesn't behave the way you expected, run `Xray` on `mp.xray.loading.Scenario` and look at what that line compiled to.

## Part 5: Life on the Stack

Each method call gets a **frame** on the call stack, which holds that call's parameters and local variables. The frame is removed when the method returns. The stack has a fixed size, and when it fills up, the JVM throws a `StackOverflowError`.

You will need to implement two methods in `StackLab`.

### whoCalledMe

`whoCalledMe` should return the name of the method that called it. `Thread.currentThread().getStackTrace()` (`stackTrace` in Kotlin) returns the current stack as an array, with the top of the stack first. Print the whole array once before you decide which index you need.

### maxRecursionDepth

`maxRecursionDepth` should measure how many calls fit on the stack. Write a helper that increments `depth` and then calls itself with no base case, call it inside a `try` block, and catch the `StackOverflowError`. Remember to reset `depth` to 0 at the start.

When both pass, run `StackLab.main`. It runs your code on threads with a 256 KB stack and an 8 MB stack so you can compare them.

## Part 6: The JIT Compiler

The JVM starts out interpreting bytecode one instruction at a time. When a method runs often enough, the JIT (just-in-time) compiler translates it into native machine code, which runs much faster.

Run `Warmup`, which times the same work over ten rounds. Then add `-Xint` to the VM options for `Warmup` in IntelliJ (Run, Edit Configurations, Modify options, Add VM options) and run it again. `-Xint` turns the JIT compiler off. Answer the questions in `REFLECTION.md` using what you saw. Remove `-Xint` when you are done.

## Testing Your Code

```
./gradlew test
```

The tests in `XrayTest` are the same tests used for grading. Part 6 is graded by course staff for completion.

| Part | Points |
|---|---|
| Part 1 | 15 |
| Part 2 | 15 |
| Part 3 | 30 |
| Part 4 | 15 |
| Part 5 | 20 |
| Part 6 | 5 |

## Handing in Your Code

The following files are used for grading:

- `ClassFiles`
- `Inspector`
- `Answers`
- `StackLab`
- `REFLECTION.md`

## Further Reading

None of these are required for the MP. They are useful if you want to know more about something you ran into.

- [JVM Specification, Chapter 4: The class File Format](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-4.html). Section 4.1 covers the header from Part 1.
- [JVM Specification, Chapter 2: The Structure of the JVM](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html). Frames, the stack, the heap, and the special method names `<init>` and `<clinit>`.
- [The `javap` command](https://docs.oracle.com/en/java/javase/21/docs/specs/man/javap.html)
- [Java Language Specification, Chapter 12: Execution](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html). Section 12.4.1 is the exact rule behind Part 4.
- [JEP 395: Records](https://openjdk.org/jeps/395)
- [JEP 280: Indify String Concatenation](https://openjdk.org/jeps/280)
- [Kotlin: Data classes](https://kotlinlang.org/docs/data-classes.html)
- [Kotlin: Calling Kotlin from Java](https://kotlinlang.org/docs/java-to-kotlin-interop.html). See "Package-level functions".
- [The `java` command](https://docs.oracle.com/en/java/javase/21/docs/specs/man/java.html). Documents `-Xint`, `-Xss`, `-Xmx` and `-Xlog`.
- [HotSpot Garbage Collection Tuning Guide](https://docs.oracle.com/en/java/javase/21/gctuning/)
