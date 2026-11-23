package mp.xray.loading

/*
 * Part 4. Read run() and predict the log it produces.
 * Write your prediction in Answers.PREDICTED_LOG BEFORE you run anything.
 *
 * The JVM only initializes a class once per program run,
 * so the tests call run() exactly once.
 */
object Scenario {
    fun run(): List<String> {
        Log.add("start")

        val nothingYet: Rocket? = null
        Log.add("declared a variable")

        val speed = Rocket.MAX_SPEED
        Log.add("read MAX_SPEED $speed")

        val fleet = arrayOfNulls<Rocket>(3)
        Log.add("made an array of ${fleet.size}")

        Rocket.launch()
        Log.add("after launch")

        Rocket()
        Rocket()
        Log.add("end")

        return Log.events()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        for (event in run()) {
            println(event)
        }
    }
}
