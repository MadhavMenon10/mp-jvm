package mp.xray.fixtures;

public class Greeter {
    public static final int SECONDS_PER_DAY = 60 * 60 * 24;

    public static int secondsPerDay() {
        return SECONDS_PER_DAY;
    }

    public static String shout(String text) {
        return text.toUpperCase() + "!";
    }

    public static String greet(String name) {
        return shout("hello " + name);
    }

    public static Runnable makeTask() {
        return () -> System.out.println("task ran");
    }
}
