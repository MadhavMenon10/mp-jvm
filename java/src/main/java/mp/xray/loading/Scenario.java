package mp.xray.loading;

import java.util.List;

/*
 * Part 4. Read this method and predict the log it produces.
 * Write your prediction in Answers.PREDICTED_LOG BEFORE you run anything.
 *
 * The JVM only runs a class's static block once per program run,
 * so the tests call run() exactly once.
 */
public final class Scenario {
    private Scenario() {
    }

    public static List<String> run() {
        Log.add("start");

        Rocket nothingYet = null;
        Log.add("declared a variable");

        int speed = Rocket.MAX_SPEED;
        Log.add("read MAX_SPEED " + speed);

        Rocket[] fleet = new Rocket[3];
        Log.add("made an array of " + fleet.length);

        Rocket.launch();
        Log.add("after launch");

        new Rocket();
        new Rocket();
        Log.add("end");

        return Log.events();
    }

    public static void main(String[] args) {
        for (String event : run()) {
            System.out.println(event);
        }
    }
}
