package mp.xray.loading;

import java.util.ArrayList;
import java.util.List;

public final class Log {
    private static final List<String> EVENTS = new ArrayList<>();

    private Log() {
    }

    public static void add(String event) {
        EVENTS.add(event);
    }

    public static List<String> events() {
        return new ArrayList<>(EVENTS);
    }
}
