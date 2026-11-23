package mp.xray.loading;

public class Rocket {
    public static final int MAX_SPEED = 9000;

    static {
        Log.add("Rocket class initialized");
    }

    public Rocket() {
        Log.add("Rocket built");
    }

    public static void launch() {
        Log.add("launch");
    }
}
