package mp.xray.loading

class Rocket {
    init {
        Log.add("Rocket built")
    }

    companion object {
        const val MAX_SPEED = 9000

        init {
            Log.add("Rocket class initialized")
        }

        fun launch() {
            Log.add("launch")
        }
    }
}
