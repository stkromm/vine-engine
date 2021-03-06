package vine.game;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import vine.util.Log;
import vine.util.reflection.VineClass;
import vine.util.reflection.ReflectUtils;
import vine.util.time.TimerManager;

/**
 * Base game class. Every gameplay class inherits this. Classes, that inherit
 * this class can be displayed in the editor.
 * 
 * @author Steffen
 *
 */
public abstract class GameObject implements Comparable<GameObject>
{
    private final Set<GameObjectCallback> onDestroyCallbacks = new HashSet<>();
    static final String                   UPDATE_METHOD      = "onUpdate";
    static final String                   CONSTRUCT_METHOD   = "construct";
    /**
     * If this flag is set, the update method of this gameobject gets executed.
     */
    private static final byte             ACTIVE_FLAG        = 2;
    private static final byte             DESTROYED_FLAG     = 4;
    private static final byte             PERSISTENCE_FLAG   = 8;
    private String                        name;
    private byte                          flags              = GameObject.ACTIVE_FLAG;

    /**
    *
    */
    @FunctionalInterface
    public interface GameObjectCallback
    {
        /**
         * @param gameObject
         *            The gameobject, that states callback changed.
         */
        void changedState(GameObject gameObject);
    }

    /**
     * @param name
     *            The name of the gameobject. Id in the game.
     */
    void setName(final String name)
    {
        this.name = name;
    }

    /**
     * @return The name identifier of this object. The name is the unique
     *         identifier to get a gameobject throughout the game. As said, the
     *         name has to be unique. If you try to create an object, that
     *         correspond to the name of an existing object, the instantiation
     *         fails.
     */
    public final String getName()
    {
        return this.name;
    }

    /**
     * @param flags
     *            The flags that should be enabled for this gameobject.
     */
    protected final void enableFlags(final byte newFlags)
    {
        if (!isDestroyed())
        {
            this.flags |= newFlags;
        }
    }

    /**
     * Sets the given flags and disables them for this gameobject.
     * 
     * @param flags
     *            The flags, that should be disabled for this gameobject.
     */
    protected final void disableFlags(final byte newFlags)
    {
        if (!isDestroyed())
        {
            this.flags &= ~newFlags;
        }
    }

    public final void activate()
    {
        enableFlags(GameObject.ACTIVE_FLAG);
    }

    public final void deactivate()
    {
        disableFlags(GameObject.ACTIVE_FLAG);
    }

    public void wait(final float seconds)
    {
        disableFlags(GameObject.ACTIVE_FLAG);
        TimerManager.get().createTimer(seconds, 1, () -> enableFlags(GameObject.ACTIVE_FLAG));
    }

    public boolean isActive()
    {
        return (this.flags & GameObject.ACTIVE_FLAG) == GameObject.ACTIVE_FLAG;
    }

    /**
     * @return True, if the corresponding flag is set.
     */
    public final boolean isDestroyed()
    {
        return (this.flags & GameObject.DESTROYED_FLAG) == GameObject.DESTROYED_FLAG;
    }

    /**
     * @return True, if the corresponding flag is set.
     */
    public boolean isLevelPersistent()
    {
        return (this.flags & GameObject.PERSISTENCE_FLAG) == GameObject.PERSISTENCE_FLAG;
    }

    public void makePersistent()
    {
        enableFlags(GameObject.PERSISTENCE_FLAG);
    }

    public void makeTemporary()
    {
        disableFlags(GameObject.PERSISTENCE_FLAG);
    }

    /**
     * @param callback
     *            Callback for destruction
     */
    public final void registerDestructionCallback(final GameObjectCallback callback)
    {
        if (!isDestroyed() && callback != null)
        {
            this.onDestroyCallbacks.add(callback);
        }
    }

    /**
     * @param callback
     *            Callback for destruction
     */
    public final void unregisterDestructionCallback(final GameObjectCallback callback)
    {
        if (!isDestroyed() && callback != null)
        {
            this.onDestroyCallbacks.remove(callback);
        }
    }

    /**
     * @param delta
     *            Time that passed since the last update call.
     */
    public final void update(final float delta)
    {
        if ((this.flags & (GameObject.ACTIVE_FLAG | GameObject.DESTROYED_FLAG)) == (GameObject.DESTROYED_FLAG
                | GameObject.ACTIVE_FLAG))
        {
            return;
        }
        onUpdate(delta);
    }

    public abstract void onUpdate(final float delta);

    public abstract void begin();

    protected abstract void onDestroy();

    public abstract void construct();

    /**
     * Use this method to destroy a GameObject that you don't need anymore. If
     * you don't use it, the GameObject remains in memory, causing a memory
     * leak.
     */
    public final void destroy()
    {
        enableFlags(GameObject.DESTROYED_FLAG);
        onDestroy();
        synchronized (this)
        {
            for (final GameObjectCallback callback : this.onDestroyCallbacks)
            {
                callback.changedState(this);
            }
            // Remove the hardreference of the gameobject
            ReferenceManager.OBJECTS.remove(this.name);
            Log.debug("Destroyed GameObject: " + toString());
        }
    }

    @Override
    public boolean equals(final Object object)
    {
        return object != null && object instanceof GameObject ? //
                this.name.equals(((GameObject) object).getName())//
                : //
                false;
    }

    @Override
    public int compareTo(final GameObject o)
    {
        if (o.getName().equals(getName()))
        {
            return 0;
        }
        if (o.hashCode() > hashCode())
        {
            return -1;
        }
        return 1;
    }

    @Override
    public int hashCode()
    {
        return this.name.hashCode();
    }

    @Override
    public String toString()
    {
        return this.getClass().getSimpleName() + "@" + this.name;
    }

    /**
     * @author Steffen
     *
     */
    static class ReferenceManager
    {
        /**
         * 
         */
        protected static final String                  ID_QUALIFIER = "?id";

        private static long                            nameGenCount;
        /**
         * All independent gameobjects that are currently in the game.
         */
        protected static final Map<String, GameObject> OBJECTS      = new WeakHashMap<>(1000);

        private ReferenceManager()
        {
        }

        /**
         * @param type
         *            The type of the instantiate gameobject.
         * @return A valid gameobject name id.
         */
        protected static final synchronized <T extends GameObject> String generateObjectName(final Class<T> type)
        {
            final String name = type.getSimpleName() + ReferenceManager.ID_QUALIFIER + ReferenceManager.nameGenCount;
            ReferenceManager.nameGenCount++;
            return name;
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
        protected static final <T extends GameObject> WeakReference<T> instantiate(
                final World world,
                final Class<T> type,
                final String name,
                final Object... params)
        {
            final VineClass<T> objectClass = new VineClass<>(type);
            final T object = objectClass.instantiate();
            if (object != null)
            {
                object.setName(name);
                ReferenceManager.OBJECTS.put(name, object);
                objectClass.getMethod(GameObject.CONSTRUCT_METHOD)
                        .ifPresent(method -> ReflectUtils.invokeMethodOn(method, object, params));
                if (objectClass.hasMethodImplemented(GameObject.UPDATE_METHOD, float.class))
                {
                    world.addObject(object);
                }
            }
            return new WeakReference<>(object);
        }

        /**
         * @param name
         *            The name of a potential gameObject
         * @return True, if a gameObject can be instantiate with the given name
         */
        static boolean isValidGameObjectName(final String name)
        {
            return name != null && !name.contains(GameObject.ReferenceManager.ID_QUALIFIER)
                    && !ReferenceManager.OBJECTS.containsKey(name);
        }
    }

}
