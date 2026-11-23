package mp.xray

/*
 * Provided, ungraded (Part 6). Run it and look at the numbers.
 * The same function runs 10 times on the same input. Why are the first
 * rounds so much slower than the last ones?
 *
 * Then run it again with the JVM flag -Xint (interpreter only, no JIT)
 * and compare. In IntelliJ: Run > Edit Configurations > VM options.
 */
object Warmup {
    fun isPrime(n: Int): Boolean {
        var d = 2
        while (d * d <= n) {
            if (n % d == 0) {
                return false
            }
            d++
        }
        return true
    }

    fun countPrimes(limit: Int): Int {
        var count = 0
        for (n in 2 until limit) {
            if (isPrime(n)) {
                count++
            }
        }
        return count
    }

    @JvmStatic
    fun main(args: Array<String>) {
        for (round in 1..10) {
            val start = System.nanoTime()
            val primes = countPrimes(20_000)
            val micros = (System.nanoTime() - start) / 1_000
            println(String.format("round %2d: %,8d us   (%d primes)", round, micros, primes))
        }
    }
}
