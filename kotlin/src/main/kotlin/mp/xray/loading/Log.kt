package mp.xray.loading

object Log {
    private val events = mutableListOf<String>()

    fun add(event: String) {
        events.add(event)
    }

    fun events(): List<String> {
        return events.toList()
    }
}
