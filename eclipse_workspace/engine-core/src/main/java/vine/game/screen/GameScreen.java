package vine.game.screen;

import vine.math.Matrix4f;
import vine.window.Window;

/**
 * @author Steffen
 *
 */
public class GameScreen implements Screen {

    private final Window window;
    private final Viewport viewport;
    public int height;
    public int width;
    private final Matrix4f projection;

    /**
     * @param window
     * @param aspectRatio
     * @param width
     */
    public GameScreen(Window window, float aspectRatio, int width) {
        super();
        this.window = window;
        this.viewport = new Viewport();
        this.height = (int) (width * aspectRatio);
        this.width = width;
        this.projection = Matrix4f.orthographic(-width / 2.f, width / 2.f, -this.height / 2.f, this.height / 2.f, -1.0f,
                1.0f);
    }

    @Override
    public Viewport getViewport() {
        return viewport;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public float worldXCoordToScreenXCoord(float x) {
        return x * getUnitsPerPixel();
    }

    @Override
    public float worldYCoordToScreenYCoord(float y) {
        return y * getUnitsPerPixel();
    }

    @Override
    public float getUnitsPerPixel() {
        return width / (float) window.getWidth();
    }

    @Override
    public float getAspect() {
        return window.getHeight() / (float) window.getWidth();
    }

    @Override
    public Matrix4f getOrthographicProjection() {
        return projection;
    }

}
