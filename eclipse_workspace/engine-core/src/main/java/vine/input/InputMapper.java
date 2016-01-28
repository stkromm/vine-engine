package vine.input;

import vine.event.EventDispatcher;
import vine.event.KeyEvent;
import vine.event.MouseButtonEvent;

/**
 * @author Steffen
 *
 */
public final class InputMapper {
    /**
     * 
     */
    private static final boolean[] KEYS = new boolean[65536];

    private InputMapper() {

    }

    /**
     * Sets the key, with the given keycode, if valid, to the given value, that
     * tells if the button is pressed.
     * 
     * @param keycode
     *            Maps to the current key state.
     * @param value
     *            Signals, that the key with the given keycode is pressed.
     */
    private static void setKeyPressed(final int keycode, final boolean value) {
        KEYS[keycode] = value;
    }

    /**
     * @return Returns the absolute number of possible different keys.
     */
    private static int getNumberOfKeys() {
        return KEYS.length;
    }

    /**
     * Returns true, if the key is currently pressed.
     * 
     * @param keycode
     *            Keycode, of the key, that press-state should be returned.
     * @return Returns true, if the key, that corresponds to the keycode, is
     *         pressed.
     */
    public static boolean isKeyPressed(final int keycode) {
        if (KEYS.length <= keycode || keycode < 0) {
            return false;
        } else {
            return KEYS[keycode];
        }
    }

    /**
     * @param input
     * @param dispatcher
     */
    public static void initInput(final Input input, final EventDispatcher dispatcher) {
        input.setKeyCallback((win, key, scancode, action, mods) -> {
            if (getNumberOfKeys() > key && key >= 0) {
                setKeyPressed(key, input.isReleaseAction(action));
            }
            dispatcher.dispatch(new KeyEvent(key, scancode, InputAction.getTypeByAction(action), mods));
        });
        input.setMouseButtonCallback((win, key, action, mods) -> {
            if (getNumberOfKeys() > key && key >= 0) {
                setKeyPressed(key, input.isReleaseAction(action));
            }
            dispatcher.dispatch(new MouseButtonEvent(key, action, mods, input.getCursorX(), input.getCursorY()));
        });
    }
}