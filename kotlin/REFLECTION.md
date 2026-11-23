# Part 6: Reflection

Write two or three sentences for each question, directly under it. There are no trick questions.
We are looking for honest thinking about what you saw, not perfect answers.

## 1. The JIT

Run `Warmup` normally, then run it again with the VM option `-Xint`. Paste both sets of numbers here.

In the normal run, why do the rounds get faster even though the code and the input never change?
Why doesn't that happen with `-Xint`?

## 2. The stack

Paste the three numbers from `StackLab.main()`.

Why does the thread with the 256 KB stack fit fewer calls than the thread with the 8 MB stack?
What is stored on the stack for each call to your recursive method?

## 3. Lazy loading

Paste your FIRST prediction for Part 4 here, even if it was wrong.

Reading `Rocket.MAX_SPEED` did not initialize `Rocket`, but calling `Rocket.launch()` did.
Look at the bytecode of `Scenario.run` (run `Xray` with the argument `mp.xray.loading.Scenario`).
Why didn't the JVM need `Rocket` to read the constant?

## 4. The big picture

Your Kotlin code and a Java program both turn into `.class` files that start with `CAFEBABE`.
In one or two sentences: what does the JVM actually run, Java, Kotlin, or something else?

## 5. Kotlin only: two version numbers

In the lecture demo, a class compiled with plain `kotlinc` had major version 52 (Java 8). In this
project, Gradle compiles your Kotlin with major version 61 (Java 17). Both run fine on a Java 21 JVM.

Why can a newer JVM run class files made for an older Java version?
Why might a compiler choose to make old-version class files on purpose?
