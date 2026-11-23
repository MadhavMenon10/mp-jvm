# MP: X-Ray the JVM

You have been writing Java or Kotlin all semester. You type some code, press the green Run button, and something happens. In this MP you find out **what actually happens between pressing Run and seeing your output.**

You won't write assembly, and you won't build anything complicated. You will:

- open up the files the compiler makes from your code and read what is inside them,
- ask the running JVM questions about your own classes,
- catch the compiler writing code you never typed,
- and push the JVM until it runs out of room, on purpose.

**Which folder do I use?** Use the `java` folder if you take the course in Java, or the `kotlin` folder if you take it in Kotlin. You can ignore the other one. Both tracks have the same parts and the same points. A few questions are different, because the Java and Kotlin compilers make different choices, and seeing those differences is part of the fun.

---

## Background: what happens when you press Run

You don't need to memorize this section. Read it once, and come back to it whenever a part of the MP mentions one of these words.

### Step 1: The compiler turns your code into bytecode

Your computer's processor cannot read Java or Kotlin. It only understands very simple instructions like "add these two numbers" or "jump to this spot".

So when you press Run, IntelliJ first runs a **compiler**: `javac` for Java, or `kotlinc` for Kotlin. The compiler reads your `.java` or `.kt` file and writes a **`.class` file**. A class file holds **bytecode**, a list of simple instructions written for a made-up computer called the **Java Virtual Machine (JVM)**.

Here is a tiny method, and the bytecode the compiler produces for it:

```java
static int square(int x) {        //    0: iload_0     push x onto the stack
    return x * x;                 //    1: iload_0     push x again
}                                 //    2: imul        multiply the top two values
                                  //    3: ireturn     return the result
```

You will read output like the right-hand side in Part 3. You don't have to memorize any instructions. Their names usually say what they do.

### Step 2: The JVM runs the bytecode

The JVM is a program, and it is already installed on your computer as part of the JDK. It reads class files and runs their bytecode. The same `.class` file runs on Windows, Mac and Linux, because each of those has its own JVM that knows how to talk to its processor.

```
  Greeter.java  ──javac──▶  Greeter.class   ─┐
                                             ├──▶  JVM  ──▶  your processor
  Greeter.kt    ─kotlinc─▶  GreeterKt.class  ┘
```

Look closely at the picture. **Java and Kotlin produce the same kind of file.** The JVM has no idea which language you wrote. That is why Kotlin code can use any Java library, and the other way around.

### Step 3: The JVM does a lot of work while your program runs

Four JVM jobs show up in this MP:

| Word | What it means | Where you see it |
|---|---|---|
| **Class loading** | The JVM reads a class file into memory and sets the class up. It only does this when a class is first needed, not when the program starts. | Part 4 |
| **The stack** | Every time you call a method, the JVM saves that method's local variables in a block of memory called a **frame**. Frames pile up on the **call stack** and are removed when the method returns. | Part 5 |
| **The JIT compiler** | At first the JVM reads your bytecode one instruction at a time, which is slow. When it notices a method running a lot, it translates that method into real processor instructions, which are much faster. JIT stands for "just in time". | Part 6 |
| **Garbage collection** | When no variable points to an object anymore, the JVM frees its memory for you automatically. | The lecture demo |

---

## Setup (about 5 minutes)

1. **Get the code.** Clone this repository (or click Code, then Download ZIP, and unzip it).
2. **Open the project.** In IntelliJ, choose File, then Open, and select the `java` or `kotlin` folder, **not** the top folder of the repository. If IntelliJ asks, choose "Open as Gradle project" and trust the project. You need JDK 17 or newer, which you already have from earlier MPs.
3. **Run the tests.** Open `src/test/.../XrayTest`, then click the green arrow next to `class XrayTest`. (If you like the terminal, `./gradlew test` inside the `java` or `kotlin` folder does the same thing. On Windows, use `gradlew.bat test`.) **Every test should fail.** That is correct: you haven't written anything yet. Your goal is to turn them green one part at a time.
4. **Run the X-ray tool.** Open `src/main/.../Xray` and click the green arrow next to `main`. It prints the bytecode of the classes in the `fixtures` folder. It also prints a message asking you to finish Part 1. Scroll through the output. It probably looks like nonsense now, and by Part 3 you will be able to read it.

**Tip:** each test is named after its part, like `part1_magicOnFakeHeader`. You can run one test at a time with the green arrow next to it. Do this often.

**Optional, if you like the terminal:** `Xray` runs a JDK tool called `javap` for you. You can also run it yourself after building:

```
javap -c -p -cp build/classes/java/main mp.xray.fixtures.Greeter          # Java track
javap -c -p -cp build/classes/kotlin/main mp.xray.fixtures.GreeterKt      # Kotlin track
```

---

## What is in the project

| File | Do you edit it? | What it is for |
|---|---|---|
| `ClassFiles` | **Yes**, Part 1 | Reads the start of a `.class` file |
| `Inspector` | **Yes**, Part 2 | Asks the JVM what is inside a class |
| `Answers` | **Yes**, Parts 3 and 4 | Where you type your detective answers |
| `StackLab` | **Yes**, Part 5 | Experiments with the call stack |
| `REFLECTION.md` | **Yes**, Part 6 | A few short written answers |
| `Xray` | No | Prints a class's header and bytecode. Run it a lot. |
| `Warmup` | No | Times the same code over and over, for Part 6 |
| `fixtures/` | **No, don't change these** | Small classes for you to investigate |
| `loading/` | No | The puzzle for Part 4 |

Every method you need to write is marked `TODO` and currently throws an error (Java) or calls `TODO(...)` (Kotlin). Replace that line with your code. The comment above each `TODO` explains the method again, with an example and steps, so you don't need to keep switching back to this README.

---

## Part 1: Hello, CAFEBABE (about 15 minutes, 15 points)

**Goal:** read the first few bytes of a real class file and work out which Java version it was made for.

A class file is not text, so you can't open it in a text editor. It is a list of **bytes**, and each byte is a number from 0 to 255. People usually write bytes in **hexadecimal** (base 16), using the digits 0 to 9 and then A to F. Each byte is exactly two hex digits. For example, 202 is `CA` and 254 is `FE`.

Every class file starts with the same 8 bytes:

| Byte positions | What they mean |
|---|---|
| 0, 1, 2, 3 | The **magic number**. It is identical in every class file ever made: `CA FE BA BE`. The JVM checks it to make sure a file really is a class file. (Yes, someone in the 1990s chose it as a joke.) |
| 4, 5 | The minor version. You can ignore it. |
| 6, 7 | The **major version**, a number that says which Java version the file was compiled for |

Open `ClassFiles` and implement these three methods, in order:

### 1.1 `magic(classFile)`

Return the first four bytes as a single string of hex digits, such as `"CAFEBABE"`.

- Loop over positions 0 to 3.
- Convert each byte to two hex digits with `String.format("%02X", value)` in Java or `"%02X".format(value)` in Kotlin. The `02` means "always use two digits", and `X` means "uppercase hex".
- Join the four pieces together and return the result.

**Watch out:** bytes in Java and Kotlin are *signed*, which means they run from -128 to 127 instead of 0 to 255. The byte `CA` (202) is actually stored as -54. Always pass the byte through the provided `unsigned(...)` method before you use it. If you skip that and put the byte in an `int` variable, your output will contain `FFFFFF`.

**Check:** run `part1_magicOnFakeHeader` and `part1_magicOnRealClass`.

### 1.2 `majorVersion(classFile)`

The major version is stored in **two** bytes, at positions 6 and 7, because one byte can't hold big numbers. Combine them like this:

```
majorVersion = (byte 6) * 256 + (byte 7)
```

It works like place value in normal numbers: in 65, the 6 is worth ten times more than the 5. Here, byte 6 is worth 256 times more than byte 7.

**Check:** run `part1_majorVersion`.

### 1.3 `javaVersion(major)`

Turn a major version into a Java release number. You have three real examples:

| Major version | Java release |
|---|---|
| 52 | 8 |
| 61 | 17 |
| 65 | 21 |

Find the pattern. The answer is one line of code.

**Check:** run every `part1_` test. All five should pass.

**Now try this:** run `Xray` again. The header prints above each class's bytecode. Which Java version was *your* code compiled for? Kotlin track: compare your answer with the lecture demo, where plain `kotlinc` produced Java 8 files.

---

## Part 2: What did the compiler write? (about 15 minutes, 15 points)

**Goal:** discover that the compiler adds code you never typed.

A running program can ask the JVM about any class: "What methods do you have? How many constructors?" This is called **reflection**. You start from a `Class` object, which you get with `Point.class` in Java or `Point::class.java` in Kotlin.

Open `Inspector` and implement these three methods.

### 2.1 `methodNames(cls)`

Return a list with the name of every method declared in the class, sorted alphabetically.

- `cls.getDeclaredMethods()` (Kotlin: `cls.declaredMethods`) gives you an array of `Method` objects.
- Each `Method` has `getName()` (Kotlin: `.name`).
- Put the names in a list, sort it, and return it.

### 2.2 `syntheticMethodNames(cls)`

Same as 2.1, but only include methods where `isSynthetic()` (Kotlin: `.isSynthetic`) is true.

**Synthetic** means "made up by the compiler". These methods exist in the class file, but you can't find them anywhere in the source code.

### 2.3 `constructorCount(cls)`

Return how many constructors the class has. Use `getDeclaredConstructors()` (Kotlin: `declaredConstructors`).

**Check:** run every `part2_` test.

**Now try this:** add a quick `main` method somewhere (or use a scratch test) and print `Inspector.methodNames(...)` for the class `Point`. Then open `fixtures/Point`. It is **one line long.** Count how many methods came out, and look at their names. Who wrote them?

---

## Part 3: Bytecode detective (about 25 minutes, 30 points)

**Goal:** learn to read the compiler's output well enough to answer questions about it.

Open `Answers`. Under "Part 3" there are ten questions. Each one has a placeholder (`"?"`, `-1` or `null`) that you replace with your answer. **Do not guess.** Every answer is visible in the `Xray` output or with your `Inspector`.

### How to read the Xray output

Here is a shortened piece of real output, with notes:

```
==================== mp.xray.fixtures.Greeter       <- which class this is
magic:         CAFEBABE                             <- your Part 1 code!
major version: 61 (Java 17)

public class mp.xray.fixtures.Greeter {
  public static int secondsPerDay();                <- a method
    Code:                                           <- its bytecode starts here
       0: ldc           #9    // int 86400          <- instruction, then a helpful comment
       2: ireturn
```

- Each method's bytecode is listed under `Code:`, one instruction per line.
- The number at the start of a line (`0:`, `2:`) is just the instruction's position. You can ignore it.
- The text after `//` is a comment that `javap` adds for you. It says what the instruction refers to, such as a method name, a number or a string. **Most answers are in these comments.**
- Instructions that call a method all start with `invoke`. There are several kinds.
- `<init>` and `<clinit>` are special method names. Working out what they mean is question 1.

### Strategy

1. Read the question in `Answers`. It tells you which class and which method to look at.
2. Run `Xray` and scroll to that class (look for the line of `=` signs).
3. Find the method, and read its instructions and comments.
4. Type your answer, then run that question's test (for example `part3_q4`).

**Kotlin track:** `Greeter.kt` has functions but no class, and the JVM can only run code that lives in a class. So where did those functions go? `Xray` prints the answer near the top of the Greeter section.

**Check:** run every `part3_` test.

---

## Part 4: Lazy loading (about 10 minutes, 15 points)

**Goal:** see that the JVM only sets up a class at the moment it is really needed.

Open `loading/Rocket` and `loading/Scenario`.

- `Rocket` contains a **static block** (Kotlin: a `companion object` with an `init` block). That code runs when the JVM **initializes** the class, which happens at most once per program run. It writes `"Rocket class initialized"` to a log.
- `Scenario.run()` uses `Rocket` in several different ways, and writes its own message to the log after each step.

**Your job: predict the log.**

1. **Before running anything,** read `Scenario.run()` line by line. For each line, ask yourself: "Does the JVM need the `Rocket` class to be set up to do this?"
2. In `Answers.PREDICTED_LOG`, write every message you expect, in order, one string per line. Copy the text exactly. The first message, `"start"`, is filled in for you.
3. Run `Scenario.main()` and compare.
4. If you were wrong, that's normal, and most people are. Keep your first guess in a comment, fix the list, and explain the surprise in `REFLECTION.md`.

**Hint for the surprising line:** run `Xray` with the argument `mp.xray.loading.Scenario` (in IntelliJ: Run, then Edit Configurations, then Program arguments). Look at what the line that reads `MAX_SPEED` turned into. Is `Rocket` mentioned there at all?

**Check:** run `part4_predictedLogMatchesTheJvm`.

---

## Part 5: Life on the stack (about 20 minutes, 20 points)

**Goal:** see the call stack for yourself, and measure how big it is.

Every time a method is called, the JVM creates a **frame** to hold that call's parameters and local variables, and puts it on top of the **call stack**. When the method returns, the frame is removed. If `main` calls `a`, and `a` calls `b`, the stack looks like this:

```
| b |   <- top: the method running right now
| a |
| main |
```

Open `StackLab` and implement two methods.

### 5.1 `whoCalledMe()`

Return the name of the method that called `whoCalledMe`.

`Thread.currentThread().getStackTrace()` (Kotlin: `Thread.currentThread().stackTrace`) gives you the current stack as an array, top first. Each element has `getMethodName()` (Kotlin: `.methodName`).

**Don't guess the index.** First print the whole array in a loop and run the test once. Look at the first few entries. One of them is the caller, and the entries above it may surprise you. Then return the right one and remove your print loop.

### 5.2 `maxRecursionDepth()`

Normally, recursion without a base case is a bug: the stack fills up and the JVM throws `StackOverflowError`. This time you do it **on purpose**, to measure the stack.

1. Write a small helper method, for example `dive()`, that adds 1 to the `depth` field and then calls itself. Give it no base case.
2. In `maxRecursionDepth`, set `depth` back to 0, then call `dive()` inside a `try` block.
3. Catch `StackOverflowError`, and return `depth`.

**Check:** run every `part5_` test.

**Now try this:** run `StackLab.main()`. It uses the provided `depthWithStackSize` to run your code on new threads with a small stack (256 KB) and a large stack (8 MB). Compare the three numbers. You will explain them in `REFLECTION.md`.

---

## Part 6: Watch the JIT (about 10 minutes, 5 points, written)

**Goal:** watch the JVM speed up your code while it runs.

1. Run `Warmup`. It runs the same prime-counting code 10 times and prints how long each round took. Look at round 1 compared with round 10.
2. Now turn the JIT compiler off. In IntelliJ, choose Run, then Edit Configurations, select `Warmup`, click "Modify options", turn on "Add VM options", and type `-Xint` in the new box. Run it again.
3. Answer the questions in `REFLECTION.md`. Two or three sentences each is plenty.

Remember to remove `-Xint` afterwards, or everything you run from that configuration stays slow.

---

## Submitting

Submit your whole `java` or `kotlin` folder, including `REFLECTION.md`. Before you submit:

- Run all of `XrayTest` one last time. Every test should be green.
- Make sure you didn't change anything in `fixtures/` or `loading/`. The tests depend on those files staying exactly the same.

## Grading

The tests in `XrayTest` are the same ones we grade with. For Part 3 they store a scrambled fingerprint (a hash) of each answer instead of the answer itself, so you can check your work without the test giving anything away. Reading the test file won't help you, but reading the bytecode will.

| Part | Points | How it is graded |
|---|---|---|
| 1. CAFEBABE | 15 | Tests |
| 2. Inspector | 15 | Tests |
| 3. Bytecode detective | 30 | Tests, 3 points per question |
| 4. Lazy loading | 15 | Test |
| 5. The stack | 20 | Tests |
| 6. Reflection | 5 | Staff, for honest effort |

## Common problems

| What you see | What it usually means |
|---|---|
| `UnsupportedOperationException: Part 1.1: magic` (Java) or `NotImplementedError` (Kotlin) | You haven't replaced that `TODO` line yet |
| Your magic number looks like `FFFFFFCAFFFFFFFE...` | A negative byte went into an `int` without `unsigned(...)`. Call `unsigned(classFile[i])` first. |
| `majorVersion` returns a huge or negative number | Same problem, or the two bytes are the wrong way around |
| The `whoCalledMe` test says `whoCalledMe` or `getStackTrace` | Your index is too small. Print the stack and look again. |
| The Part 4 test fails, but your list looks right | Check spelling, spaces and capital letters. `"read MAX_SPEED 9000"` is not the same as `"read MAX_SPEED: 9000"`. |
| A Part 3 string answer doesn't match | Answers are case sensitive and must not have extra spaces. In Kotlin, write a `$` inside a string as `\$`. |
| `javap not found` when running `Xray` | IntelliJ is using a JRE instead of a JDK. Set the project SDK to a JDK 17+ in File, then Project Structure. |

## Want to learn more?

You don't need any of these to finish the MP. They are here if something surprised you and you want to know why. The official specifications are long and technical, so skim them for the section mentioned.

**The class file and bytecode**
- [JVM Specification, Chapter 4: The class File Format](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-4.html). The official description of the file you read in Part 1. Section 4.1 has the magic number and a table of every version number.
- [JVM Specification, Chapter 2: The Structure of the JVM](https://docs.oracle.com/javase/specs/jvms/se21/html/jvms-2.html). How the JVM organizes memory: frames, the stack and the heap. It also explains `<init>` and `<clinit>`.
- [The `javap` command](https://docs.oracle.com/en/java/javase/21/docs/specs/man/javap.html). Every option of the disassembler that `Xray` uses.

**Class loading**
- [Java Language Specification, Chapter 12: Execution](https://docs.oracle.com/javase/specs/jls/se21/html/jls-12.html). Section 12.4.1, "When Initialization Occurs", is the exact rule behind Part 4.

**Code the compiler writes for you**
- [JEP 395: Records](https://openjdk.org/jeps/395). What a record is, and which methods the compiler generates for it.
- [JEP 280: Indify String Concatenation](https://openjdk.org/jeps/280). Why `"hello " + name` turns into `invokedynamic` in modern Java (Part 3, question 10).
- [Kotlin: Data classes](https://kotlinlang.org/docs/data-classes.html). The list of functions Kotlin generates for every `data class`.
- [Kotlin: Calling Kotlin from Java](https://kotlinlang.org/docs/java-to-kotlin-interop.html). The "Package-level functions" section explains where top-level functions end up.

**Running the JVM**
- [The `java` command](https://docs.oracle.com/en/java/javase/21/docs/specs/man/java.html). Explains the options from this MP: `-Xint` (no JIT), `-Xss` (stack size), `-Xmx` (heap size) and `-Xlog` (logging).
- [HotSpot Garbage Collection Tuning Guide](https://docs.oracle.com/en/java/javase/21/gctuning/). Chapter 1 explains what a garbage collector does and why there is more than one.

