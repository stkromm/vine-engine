package vine.game;

import vine.application.Application;
import vine.application.StatMonitor;
import vine.game.screen.Viewport;
import vine.graphics.Graphics;
import vine.input.Input;
import vine.input.InputMapper;
import vine.settings.GameSettings;
import vine.window.Window;

/**
 * @author Steffen
 *
 */
public class GameRunner {
    private volatile boolean running = true;
    private final Window window;
    private final Input input;
    private final Graphics graphics;

    /**
     * @param window
     *            System window
     * @param input
     *            Input Manager
     * @param graphics
     *            Graphics Provider
     */
    public GameRunner(Window window, Input input, Graphics graphics) {
        this.window = window;
        this.input = input;
        this.graphics = graphics;
    }

    /**
     * Executes the game loop.
     */
    public void run() {
        // init render thread
        Thread.currentThread().setName("render");
        graphics.makeContext(window.getContext());
        graphics.init();
        window.setSizeCallback((w, h) -> {
            window.setWindowSize(w, h);
            Viewport viewport = Game.getGame().getScreen().getViewport();
            graphics.setViewport(viewport.getLeftOffset(), viewport.getTopOffset(), w - viewport.getRightOffset(),
                    h - viewport.getBottomOffset());
        });
        InputMapper.assignInput(input);
        // init game
        Game.init(window, graphics);
        int cores = Application.getProcessorCount();
        long currentTime = 0;
        if (cores == 1) {
            while (running) {
                StatMonitor.newUp();
                input.pollEvents();
                // System.out.println(1 / ((System.nanoTime() - currentTime) /
                // 1000000000.f));
                Game.update((System.nanoTime() - currentTime) / 100000000.f);
                currentTime = System.nanoTime();
                StatMonitor.newFrame();
                graphics.clearBuffer();
                Game.render();
                graphics.swapBuffers();
                sleep((int) (GameSettings.getMaxFrameDuration() * 1000000000 - (System.nanoTime() - currentTime)));
                if (window.requestedClose()) {
                    running = false;
                }
            }
        } else if (cores >= 2) {
            final Thread logic = logicThread();
            logic.start();
            while (running) {
                StatMonitor.newFrame();
                currentTime = System.nanoTime(); // NOSONAR
                input.pollEvents();
                graphics.clearBuffer();
                Game.render();
                graphics.swapBuffers();
                sleep((int) (GameSettings.getMaxFrameDuration() * 1000000000 - (System.nanoTime() - currentTime)));
                if (window.requestedClose()) {
                    running = false;
                }
            }
            logic.interrupt();
        }
    }

    /**
     * @return A thread, used to calculate the game logic.
     */
    private Thread logicThread() {
        final Thread logic = new Thread(() -> {
            long currentTime = System.nanoTime();
            while (running) {
                StatMonitor.newUp();
                Game.update((System.nanoTime() - currentTime) / 100000000.f);
                currentTime = System.nanoTime();
                sleep((int) (GameSettings.getMaxFrameDuration() * 1000000000 - (System.nanoTime() - currentTime)));
            }
        });
        logic.setName("logic");
        return logic;
    }

    /**
     * @param sleepTime
     *            The time, the thread should sleep. It is not guaranteed the
     *            thread will sleep the given time.
     */
    private static void sleep(long sleepTime) {
        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime / 100000000L, (int) (sleepTime % 1000000L));
            } catch (InterruptedException e) {
                // do nothing
            }
        }
    }
}
