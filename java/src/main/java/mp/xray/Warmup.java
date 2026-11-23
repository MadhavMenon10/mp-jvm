package mp.xray;

/*
 * Provided, ungraded (Part 6). Run it and look at the numbers.
 * The same method runs 10 times on the same input. Why are the first
 * rounds so much slower than the last ones?
 *
 * Then run it again with the JVM flag -Xint (interpreter only, no JIT)
 * and compare. In IntelliJ: Run > Edit Configurations > VM options.
 */
public final class Warmup {
    private Warmup() {
    }

    static boolean isPrime(int n) {
        for (int d = 2; d * d <= n; d++) {
            if (n % d == 0) {
                return false;
            }
        }
        return true;
    }

    static int countPrimes(int limit) {
        int count = 0;
        for (int n = 2; n < limit; n++) {
            if (isPrime(n)) {
                count++;
            }
        }
        return count;
    }

    public static void main(String[] args) {
        for (int round = 1; round <= 10; round++) {
            long start = System.nanoTime();
            int primes = countPrimes(20_000);
            long micros = (System.nanoTime() - start) / 1_000;
            System.out.printf("round %2d: %,8d us   (%d primes)%n", round, micros, primes);
        }
    }
}
