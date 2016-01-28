package vine.game;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import vine.event.Event.EventType;
import vine.event.KeyEvent;
import vine.event.MouseButtonEvent;
import vine.reflection.VineClass;
import vine.reflection.VineMethodUtils;

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
    protected static final String CONSTRUCT_METHOD = "construct";
    /**
     * 
     */
    protected static final String KEY_EVENT_METHOD = "onKeyEvent";
    /**
     * 
     */
    protected static final String MOUSE_BUTTON_EVENT_METHOD = "onMouseButtonEvent";
    /**
     * If this flag is set, the gameobject gets not rendered.
     */
    public static final byte HIDE_FLAG = 1;
    /**
     * If this flag is set, the update method of this gameobject gets executed.
     */
    public static final byte ACTIVE_FLAG = 2;
    private static final byte DESTROYED_FLAG = 4;
    private String name;
    private byte flags = ACTIVE_FLAG;

    /**
     * @param name
     */
    protected void setName(final String name) {
        this.name = name;
    }
    /**
     * 
     */

    /**
     * @param flags
     *            The flags that should be enabled for this gameobject.
     */
    public final void enableFlags(final byte flags) {
        this.flags |= flags;
    }

    /**
     * Sets the given flags (that is 1's in the byte) and disables them for this
     * gameobject.
     * 
     * @param flags
     *            The flags, that should be disabled for this gameobject.
     */
    public final void disableFlags(final byte flags) {
        final int effectiveFlags = ~flags;
        this.flags &= effectiveFlags;
    }

    /**
     * @return True, if the corresponding flag is set.
     */
    public final boolean hideFlag() {
        return (flags & HIDE_FLAG) == HIDE_FLAG;
    }

    /**
     * @return The name identifier of this object. The name is the unique
     *         identifier to get a gameobject throughout the game. As said, the
     *         name has to be unique. If you try to create an object, that
     *         correspond to the name of an existing object, the instantiation
     *         fails.
     */
    public final String getName() {
        return name;
    }

    /**
     * @param delta
     *            Time that passed since the last update call.
     */
    public void update(final float delta) { // NOSONAR
        if ((flags & ACTIVE_FLAG) != ACTIVE_FLAG) {
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
    public final void destroy() {
        enableFlags(DESTROYED_FLAG);
        onDestroy();
        synchronized (this) {
            ReferenceManager.OBJECTS.remove(name);
        }
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
    public boolean equals(final Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof GameObject) {
            return name.equals(((GameObject) object).getName());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    /**
     * @author Steffen
     *
     */
    static class ReferenceManager {
        /**
         * 
         */
        protected static final String ID_QUALIFIER = "?id";

        private static int nameGenCount;
        /**
         * All independent gameobjects that are currently in the game.
         */
        protected static final Map<String, GameObject> OBJECTS = new ConcurrentHashMap<>();

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
        protected static final synchronized <T extends GameObject> String generateObjectName(final Class<T> type) {
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
         *            The optional arguments of the construct method of the
         *            instantiated type.
         * @return The instantated gameobject or null on failure
         */
        protected static final <T extends GameObject> T instantiate(final Class<T> type, final String name,
                final Object... params) {
            final VineClass<T> objectClass = new VineClass<>(type);
            final T object = objectClass.instantiateType();
            if (object != null) {
                object.setName(name);
                OBJECTS.put(name, object);
                objectClass.getMethodByName(CONSTRUCT_METHOD)
                        .ifPresent(method -> VineMethodUtils.invokeMethodOn(method, object, params));
                if (objectClass.hasMethodImplemented(GameObject.KEY_EVENT_METHOD, KeyEvent.class)) {
                    Game.getGame().getScene().addEventHandler(
                            event -> event.getType() == EventType.KEY ? object.onKeyEvent((KeyEvent) event) : false);
                }
                if (objectClass.hasMethodImplemented(GameObject.MOUSE_BUTTON_EVENT_METHOD, MouseButtonEvent.class)) {
                    Game.getGame().getScene().addEventHandler(event -> event.getType() == EventType.MOUSE_BUTTON
                            ? object.onMouseButtonEvent((MouseButtonEvent) event) : false);
                }
            }
            return object;
        }
    }
}