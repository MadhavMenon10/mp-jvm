package mp.xray.fixtures

// No class in this file. Where do these functions live on the JVM?

const val SECONDS_PER_DAY = 60 * 60 * 24

fun secondsPerDay(): Int {
    return SECONDS_PER_DAY
}

fun shout(text: String): String {
    return text.uppercase() + "!"
}

fun greet(name: String): String {
    return shout("hello $name")
}

fun makeTask(): Runnable {
    return Runnable { println("task ran") }
}
