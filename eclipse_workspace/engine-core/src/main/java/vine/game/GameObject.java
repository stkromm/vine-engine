package vine.game;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import vine.event.Event;
import vine.event.Event.EventType;
import vine.event.KeyEvent;
import vine.event.MouseButtonEvent;

/**
 * Base game class. Every gameplay class inherits this. Classes, that inherit
 * this class can be displayed in the editor.
 * 
 * @author Steffen
 *
 */
public abstract class GameObject {
    /**
     * 
     */
    static final String CONSTRUCT_METHOD = "construct";
    /**
     * 
     */
    static final String KEY_EVENT_METHOD = "onKeyEvent";
    /**
     * 
     */
    static final String MOUSE_BUTTON_EVENT_METHOD = "onMouseButtonEvent";
    /**
     * If this flag is set, the gameobject gets not rendered.
     */
    public static final byte HIDE_FLAG = 1;
    /**
     * If this flag is set, the update method of this gameobject gets executed.
     */
    public static final byte ACTIVE_FLAG = 2;
    private static final byte DESTROYED_FLAG = 4;
    /**
     * The name is the unique identifier to get a gameobject throughout the
     * game. As said, the name has to be unique. If you try to create an object,
     * that correspond to a given name already, the instantiation fails.
     */
    String name;
    private volatile byte flags = ACTIVE_FLAG;

    /**
     * 
     */

    /**
     * @param flags
     *            The flags that should be enabled for this gameobject.
     */
    public final void enableFlags(byte flags) {
        this.flags |= flags;
    }

    /**
     * Sets the given flags (that is 1's in the byte) and disables them for this
     * gameobject.
     * 
     * @param flags
     *            The flags, that should be disabled for this gameobject.
     */
    public final void disableFlags(byte flags) {
        int effectiveFlags = ~flags;
        this.flags &= effectiveFlags;
    }

    /**
     * @return True, if the corresponding flag is set.
     */
    public final boolean hideFlag() {
        return (flags & HIDE_FLAG) == HIDE_FLAG;
    }

    /**
     * @return The name identifier of this object.
     */
    public final String getName() {
        return name;
    }

    /**
     * @param delta
     *            Time that passed since the last update call.
     */
    public void update(float delta) { // NOSONAR
        if (!((flags & ACTIVE_FLAG) == ACTIVE_FLAG)) {
            return;
        }
    }

    /**
     * 
     */
    public void begin() {
        // Not necessarily needs to be overridden
    }

    /**
     * Use this method to destroy a GameObject that you don't need anymore. If
     * you don't use it, the GameObject remains in memory, causing a memory
     * leak.
     */
    public final synchronized void destroy() {
        enableFlags(DESTROYED_FLAG);
        onDestroy();
        ReferenceManager.objects.remove(this.name);
    }

    /**
     * Method, that is called, when this gameobject gets destroyed. If you
     * override this method, be sure to call the super method at begin. In order
     * to destroy an object, use the destroy method, not this method!
     */
    protected void onDestroy() {
        if (!((flags & DESTROYED_FLAG) == DESTROYED_FLAG)) {
            return;
        }
    }

    /**
     * @param event
     *            the event to react to
     * @return true if the event is consumned
     */
    public boolean onKeyEvent(KeyEvent event) { // NOSONAR
        return false;
    }

    /**
     * @param keyEvent
     *            The key event reacted to
     * @return true, if the event is consumed by this handler
     */
    public boolean onMouseButtonEvent(MouseButtonEvent keyEvent) { // NOSONAR
        return false;
    }

    /**
     * 
     */
    protected void construct() {
        // Only override to add functionality.
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof GameObject) {
            return getName().equals(((GameObject) object).getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    /**
     * @author Steffen
     *
     */
    static class ReferenceManager {
        private static final String ID_QUALIFIER = "?id";
        /**
         * All independent gameobjects that are currently in the game.
         */
        static final Map<String, GameObject> objects = new HashMap<>();
        private static volatile int nameGenCount = 0;

        private ReferenceManager() {
            /**
             * Hide public.
             */
        }

        /**
         * @param type
         *            The type of the instantiate gameobject.
         * @return The instantated gameobject or null on failure
         */
        static final synchronized <T extends GameObject> String generateObjectName(Class<T> type) {
            final String className = type.getName() + ID_QUALIFIER + nameGenCount;
            nameGenCount++;
            return className;
        }

        /**
         * @param name
         *            The name used to identify the object in the game.
         * @param type
         *            The type of the instantiate gameobject.
         * @param params
         *            The optinal arguments of the construct method of the
         *            instantiated type.
         * @return The instantated gameobject or null on failure
         */
        static final synchronized <T extends GameObject> T instantiate(Class<T> type, String name, Object... params) {
            T object = instantiateType(type);
            if (object == null) {
                return null;
            }
            object.name = name;
            objects.put(name, object);
            Method construct = getConstructMethod(type);
            if (construct != null) {
                try {
                    construct.invoke(object, params);
                } catch (IllegalAccessException e) {
                    Logger.getGlobal().log(Level.SEVERE, "The construct method you've implemented in the class "
                            + type.toString() + " is not public.", e);

                } catch (IllegalArgumentException e) {
                    String par = Arrays.toString(params);
                    Logger.getGlobal().log(Level.SEVERE,
                            "The supplied arguments " + par
                                    + " don't match the defined argument list of the given class " + type.toString()
                                    + " construct method.\n Be sure to supply the right number of arguments in the order they are declared in the construct method.",
                            e);

                } catch (InvocationTargetException e) {
                    Logger.getGlobal().log(Level.SEVERE,
                            "Class has not implemented construct method. Have you passed a GameObject inherited class?",
                            e);
                }
            }
            if (hasImplementedMethod(GameObject.KEY_EVENT_METHOD, type, KeyEvent.class)) {
                Game.getGame().getScene().addKeyHandler((Event e) -> {
                    if (e.getType() != EventType.KEY) {
                        return false;
                    }
                    KeyEvent keyEvent = (KeyEvent) e;
                    return object.onKeyEvent(keyEvent);
                });
            }
            if (hasImplementedMethod(GameObject.MOUSE_BUTTON_EVENT_METHOD, type, MouseButtonEvent.class)) {
                Game.getGame().getScene().addMouseButtonHandler((Event e) -> {
                    if (e.getType() != EventType.MOUSE_BUTTON) {
                        return false;
                    }
                    MouseButtonEvent keyEvent = (MouseButtonEvent) e;
                    return object.onMouseButtonEvent(keyEvent);
                });
            }
            return object;
        }

        private static <T> Method getConstructMethod(Class<T> type) {
            Method[] constructor = type.getMethods();
            for (Method m : constructor) {
                if (m.getName().equals(CONSTRUCT_METHOD)) {
                    return m;
                }
            }
            return null;
        }

        private static <T extends GameObject> T instantiateType(Class<T> type) {
            T object;
            try {
                object = type.newInstance();

            } catch (InstantiationException e) {
                Logger.getGlobal()
                        .log(Level.SEVERE,
                                "Could not instantiate gameobject of class:" + type.getName()
                                        + "\nMaybe you passed a class, that is abstract and or does not inherit Gameobject",
                                e);
                return null;
            } catch (IllegalAccessException e) {
                Logger.getGlobal().log(Level.SEVERE, "Could not instantiate gameobject of class:" + type.getName()
                        + "\n Perhaps you made the constructor private?\n", e);
                return null;
            }
            return object;
        }

        private static <T extends GameObject> boolean hasImplementedMethod(String methodName, Class<T> type,
                Class<?>... params) {
            Method method = null;
            try {
                if (params != null && params.length > 0) {
                    method = type.getMethod(methodName, params);
                } else {
                    method = type.getMethod(methodName);
                }
            } catch (NoSuchMethodException | SecurityException e) {
                Logger.getGlobal().log(Level.SEVERE, "Auto-generated catch block", e);
            }
            return method != null && method.getDeclaringClass().equals(type);
        }

    }
}
