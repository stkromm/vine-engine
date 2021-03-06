package vine.window;

import vine.util.Clipboard;

/**
 * A window, represents a system window, that exists within a display and is
 * used, to display an application.
 * 
 * @author Steffen
 *
 */
public interface Window
{

    /**
     * Callback, used to react to a close request on the window.
     * 
     * @author Steffen
     *
     */
    @FunctionalInterface
    interface CloseCallback
    {
        /**
         * Called on close request.
         */
        void onCloseRequest();
    }

    /**
     * Callback, used to react to a change of the framebuffer size.
     * 
     * @author Steffen
     *
     */
    @FunctionalInterface
    interface FramebufferSizeCallback
    {
        /**
         * @param width
         *            The new framebuffer width.
         * @param height
         *            The new framebuffer height.
         */
        void onFramebufferSizeChange(int width, int height);
    }

    /**
     * Callback, used to react to window position change.
     * 
     * @author Steffen
     *
     */
    @FunctionalInterface
    interface PositionCallback
    {
        /**
         * @param xPos
         *            The new x position of the window on the display
         * @param yPos
         *            The new y position of the window on the display
         */
        void onPositionChange(int xPos, int yPos);
    }

    /**
     * Callback, used to react to a window size change.
     * 
     * @author Steffen
     *
     */
    @FunctionalInterface
    interface SizeCallback
    {
        /**
         * @param width
         *            The new width the window changed to.
         * @param height
         *            The new height the window changed to.
         */
        void onSizeChange(int width, int height);
    }

    /**
     * Callback, used to react to a window context change.
     * 
     * @author Steffen
     *
     */
    @FunctionalInterface
    interface WindowContextCallback
    {
        /**
         * @param context
         *            The new window context
         */
        void onContextChange(long context);
    }

    /**
     * @return The current width of the window.
     */
    int getWidth();

    /**
     * @return The current height of the window.
     */
    int getHeight();

    /**
     * @return The current x position of the window on the display.
     */
    int getPosX();

    /**
     * @return The current y position of the window on the display.
     */
    int getPosY();

    /**
     * @return The current frambuffer width;
     */
    int getFramebufferWidth();

    /**
     * @return The current framebuffer height;
     */
    int getFramebufferHeight();

    /**
     * @param width
     *            The new width the window changed to.
     * @param height
     *            The new height the window changed to.
     */
    void setWindowSize(int width, int height);

    /**
     * @param xPos
     *            The new x position of the window on the display
     * @param yPos
     *            The new y position of the window on the display
     */
    void setWindowPosition(int xPos, int yPos);

    /**
     * Releases the current SizeCallback and sets the given.
     * 
     * @param callback
     *            The new SizeCallback
     */
    void setSizeCallback(SizeCallback callback);

    /**
     * Releases the current PositionCallback and sets the given.
     * 
     * @param callback
     *            The new PositionCallback
     */
    void setPositionCallback(PositionCallback callback);

    /**
     * Releases the current FramebufferSizeCallback and sets the given.
     * 
     * @param callback
     *            The new FramebufferSizeCallback
     */
    void setFramebufferSizeCallback(FramebufferSizeCallback callback);

    /**
     * Releases the current CloseCallback and sets the given.
     * 
     * @param callback
     *            The new CloseCallback
     */
    void setCloseCallback(CloseCallback callback);

    /**
     * @param callback
     *            The new context change callback
     */
    void setWindowContextCallback(WindowContextCallback callback);

    /**
     * @param title
     *            The title of the window.
     */
    void setTitle(String title);

    /**
     * @return True, if the window is resizeable by the user.
     */
    boolean isResizeable();

    /**
     * @return True, if the window is visible to the user.
     */
    boolean isVisible();

    /**
     * @return True, if the window has user focus.
     */
    boolean isFocused();

    /**
     * @return True, if the window is decorated.
     */
    boolean isDecorated();

    /**
     * @return True, if the window is currently iconified.
     */
    boolean isIconified();

    /**
     * @return True, if the user want to close the window.
     */
    boolean requestedClose();

    /**
     * Closes (destroys) the window.
     */
    void close();

    /**
     * Hides the window from the user.
     */
    void hide();

    /**
     * Shows the window to the user.
     */
    void show();

    /**
     * @return The context of the window, that is used to identify the window in
     *         the game.
     */
    long getContext();

    /**
     * @return The window title.
     */
    String getTitle();

    /**
     * @param mode
     *            The new window mode
     */
    void setWindowMode(WindowMode mode);

    Clipboard getClipboard();
}