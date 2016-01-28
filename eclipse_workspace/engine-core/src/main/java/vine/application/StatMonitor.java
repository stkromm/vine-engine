package vine.application;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Steffen
 *
 */
public final class StatMonitor {
    private static final int SAMPLE_COUNT = 100;
    private static long lastUpdateTime = System.nanoTime();
    private static long lastFrameTime = System.nanoTime();
    private static final List<Long> FRAME_DURATIONS = new ArrayList<>();
    private static final List<Long> UP_DURATIONS = new ArrayList<>();

    private StatMonitor() {

    }

    /**
     * @return The average updates per seconds the game is calculated with.
     */
    public static float getUps() {
        long frames = 0;
        for (int i = 0; i < UP_DURATIONS.size(); i++) {
            frames += UP_DURATIONS.get(i).longValue();
        }
        return 10000000000000f / frames / UP_DURATIONS.size();
    }

    /**
     * @return The average frames per second the game is rendered with.
     */
    public static float getFPS() {
        long frames = 0;
        for (int i = 0; i < FRAME_DURATIONS.size(); i++) {
            frames += FRAME_DURATIONS.get(i).longValue();
        }
        return 10000000000000f / frames / FRAME_DURATIONS.size();
    }

    /**
     * @return The memory, used by the application.
     */
    public static long getUsedMemory() {
        return Runtime.getRuntime().totalMemory();
    }

    /**
     * 
     */
    public static void newUp() {
        if (UP_DURATIONS.size() >= SAMPLE_COUNT) {
            UP_DURATIONS.remove(0);
        }
        UP_DURATIONS.add(Long.valueOf(System.nanoTime() - lastUpdateTime));
        lastUpdateTime = System.nanoTime();
    }

    /**
     * 
     */
    public static void newFrame() {
        if (FRAME_DURATIONS.size() >= SAMPLE_COUNT) {
            FRAME_DURATIONS.remove(0);
        }
        FRAME_DURATIONS.add(Long.valueOf(System.nanoTime() - lastFrameTime));
        lastFrameTime = System.nanoTime();
    }
}