package vine.input;

import vine.math.Vector2f;

public interface Input {
    @FunctionalInterface
    interface ScrollCallback {
        public void scrolled(long context, double offsetx, double offsety);
    }

    void setScrollCallback(ScrollCallback callback);

    @FunctionalInterface
    interface KeyCallback {
        public void keyPressed(long context, int key, int scancode, int action, int mods);
    }

    void setKeyCallback(KeyCallback callback);

    @FunctionalInterface
    interface CharCallback {
        public void charInput(long context, int codepoint);
    }

    void setCharCallback(CharCallback callback);

    @FunctionalInterface
    interface CharModCallback {
        public void charModInput(long context, int codepoint, int mods);
    }

    void setCharModCallback(CharModCallback callback);

    @FunctionalInterface
    interface CursorPositionCallback {
        public void changedCursorPosition(long context, double x, double y);
    }

    void setCursorPositionCallback(CursorPositionCallback callback);

    @FunctionalInterface
    interface MouseButtonCallback {
        public void pressedMouseButton(long context, int button, int action, int mods);
    }

    void setMouseButtonCallback(MouseButtonCallback callback);

    double getCursorX();

    double getCursorY();

    void listenToWindow(long context);

    void pollEvents();

    boolean isReleaseAction(int action);
}
