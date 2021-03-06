package vine.game.scene;

import vine.math.GMath;
import vine.math.vector.MutableVec2f;
import vine.math.vector.Vec2f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import vine.game.GameObject;
import vine.game.Transform;
import vine.game.World;
import vine.game.primitive.Primitive;
import vine.graphics.Color;
import vine.graphics.Renderable;
import vine.util.ConcurrentManagedSet;
import vine.util.time.TimerManager;

/**
 * @author Steffen
 *
 */
public class GameEntity extends GameObject
{
    /**
     * Lifetime constant that signals that this entity lives forever.
     */
    public static final float                            LIVE_FOREVER        = -1;
    /**
     * The lifetime of this entity. This value is literally the time the entity
     * will live.
     */
    private float                                        lifetime            = GameEntity.LIVE_FOREVER;
    /**
     * The scene that contains this entity.
     */
    private Scene                                        scene;
    /**
     * The gameplay tags of this entity. It's faster to search after a amount of
     * entity that share the same tag than search after objects of the same
     * class.
     */
    private final List<String>                           tags                = new ArrayList<>();
    /**
     * The components of this entity. The components dynamically extend the
     * functionality of this entity.
     */
    private final Set<Component>                         components          = new HashSet<>(10);
    private final Map<Class<?>, Component>               componentCache      = new HashMap<>();
    private final Set<Renderable>                        renderables         = new ConcurrentManagedSet<>(
            new HashSet<>());
    /**
     * The Chunk that the entity exists in.
     */
    private Chunk                                        currentChunk;
    private int                                          chunkX              = -1;
    private int                                          chunkY              = -1;
    private final int                                    numberOfChunks      = 10;

    // Position
    private final Transform                              transform           = new Transform();
    /**
     * The z order of this entity. A higher value means it will be rendered on
     * top of entity with a lower value.
     */
    private float                                        zPosition           = 0.2f;
    /**
     * The extends of the collision box. Origin is the world space position of
     * this entity.
     */
    private final MutableVec2f                           boundingBoxExtends  = new MutableVec2f(32, 32);

    // Appearance
    private final Color                                  color               = new Color(0, 0, 0, 0);

    private boolean                                      moveable            = true;

    private final ConcurrentManagedSet<ExecutionPayload> executionList       = new ConcurrentManagedSet<>(
            new HashSet<>());
    private final List<Primitive>                        collisionComponents = new ArrayList<>();

    @FunctionalInterface
    public interface ExecutionPayload
    {
        boolean tick(float delta);
    }

    public void addExecutionPayload(final ExecutionPayload payload)
    {
        executionList.add(payload);
    }

    public final boolean isMoveable()
    {
        return moveable;
    }

    public final void setMoveable(final boolean moveable)
    {
        this.moveable = moveable;
    }

    public Set<Renderable> getRenderables()
    {
        return renderables;
    }

    public final List<Primitive> getPrimitives()
    {
        return collisionComponents;
    }

    /**
     * @return The x Coordinate in Worldspace of this entity
     */
    public final float getXPosition()
    {
        return transform.getWorldPosition().getX();
    }

    /**
     * @return The y Coordinate in Worldspace of this entity
     */
    public final float getYPosition()
    {
        return transform.getWorldPosition().getY();
    }

    /**
     * @return The z order of this entity
     */
    public final float getZPosition()
    {
        return zPosition;
    }

    public final Vec2f getPosition()
    {
        return transform.getWorldPosition();
    }

    public final void addPosition(final float x, final float y)
    {
        transform.translate(x, y);
    }

    public final Chunk getChunk()
    {
        return currentChunk;
    }

    /**
     * Sets the position of this entity in Worldspace Coordinates.
     */
    public final void setPosition(final float x, final float y)
    {
        transform.translate(x - transform.getWorldPosition().getX(), y - transform.getWorldPosition().getY());
    }

    public void setZ(final float z)
    {
        zPosition = z;
    }

    // Color
    public final Color getColor()
    {
        return color;
    }

    public final void dye(final Color color)
    {
        this.color.setColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    public final void flash(final Color color, final float duration, final boolean smooth)
    {
        if (smooth)
        {
            // Timer with callback that uses elapsed time to transition to
            // origin color
        } else
        {
            final Color originColor = this.color;
            TimerManager.get().createTimer(duration, 1, () -> dye(originColor));
            dye(color);
        }
    }

    public final void addTransparency(final float percent)
    {
        color.addTransparency(Math.round(percent * 256));
    }

    public final void setVisible()
    {
        color.setColor(color.getRed(), color.getGreen(), color.getBlue(), 0);
    }

    public final void setInvisible()
    {
        color.setColor(color.getRed(), color.getGreen(), color.getBlue(), 1);
    }

    /**
     * @return The size of the collision box
     */
    public final Vec2f getBoundingBoxExtends()
    {
        return boundingBoxExtends;
    }

    public final float getXExtends()
    {
        return boundingBoxExtends.getX();
    }

    public final float getYExtends()
    {
        return boundingBoxExtends.getY();
    }

    public final void setBoundingBox(final float x, final float y)
    {
        boundingBoxExtends.setX(x);
        boundingBoxExtends.setY(y);
    }

    /**
     * @return The scene that contains this entity
     */
    public final Scene getScene()
    {
        return scene;
    }

    /**
     * Adds this entity to the given scene.
     */
    final void setScene(final Scene scene)
    {
        if (this.scene != null)
        {
            if (this.scene.equals(scene))
            {
                return;
            }
            scene.removeEntity(this);
        }
        this.scene = scene;
    }

    public final World getWorld()
    {
        return getScene().getWorld();
    }

    /**
     * @param tag
     *            The tag that is looked for
     * @return true, if this entity contains the given tag
     */
    public final boolean containsTag(final String tag)
    {
        return tag == null ? false : tags.contains(tag);
    }

    /**
     * @param tag
     *            The tag that should be added to this object
     */
    public final void addTag(final String tag)
    {
        if (tag != null && !containsTag(tag))
        {
            tags.add(tag);
        }
    }

    /**
     * @param tag
     *            The tag should be removed from this object
     */
    public final void removeTag(final String tag)
    {
        if (tag != null)
        {
            tags.remove(tag);
        }
    }

    /**
     * @param type
     *            The type by which components are searched
     * @return all components found of this type
     */
    public <T extends Component> List<T> getComponents(final Class<T> type)
    {
        final List<T> components = new ArrayList<>(this.components.size());
        for (final Component component : this.components)
        {
            if (type.isInstance(component))
            {
                components.add(type.cast(component));
            }
        }
        return components;
    }

    /**
     * @param type
     *            The type by which components are searched
     * @return all components found of this type
     */
    public <T extends Component> void getComponents(final Class<T> type, final List<T> list)
    {
        for (final Component component : components)
        {
            if (type.isInstance(component))
            {
                list.add(type.cast(component));
            }
        }
    }

    /**
     * @param type
     *            The type of searched component
     * @return the reference to a component, that is of the given type or null
     *         if there is non
     */
    public final <T extends Component> T getComponent(final Class<T> type)
    {
        final Component comp = componentCache.get(type);
        if (comp != null)
        {
            return type.cast(comp);
        }
        for (final Component component : components)
        {
            if (type.isInstance(component))
            {
                return type.cast(component);
            }
        }
        return null;
    }

    /**
     * Attaches the component to this entity
     */
    public final void attachComponent(final Component component)
    {
        if (component == null || isDestroyed())
        {
            return;
        }
        components.add(component);
        component.attachTo(this);
        componentCache.put(component.getClass(), component);
        component.onAttach();
        if (component instanceof Renderable)
        {
            renderables.add((Renderable) component);
        }
        if (component instanceof Primitive)
        {
            collisionComponents.add((Primitive) component);
        }
    }

    /**
     * Detaches the given component from this entity
     */
    public final void detachComponent(final Component component)
    {
        if (component == null)
        {
            return;
        }
        if (components.remove(component) && componentCache.containsValue(component))
        {
            componentCache.remove(component.getClass());
        }
        if (component instanceof Renderable)
        {
            renderables.remove(component);
        }
        if (component instanceof Primitive)
        {
            collisionComponents.remove(component);
        }
    }

    /**
     * @return True, if the given Component is attached to this entity.
     */
    public final boolean containsComponent(final Component component)
    {
        return components.contains(component);
    }

    /**
     * Clears the cache of this entity.
     */
    public final void clearComponentCache()
    {
        componentCache.clear();
    }

    public void setCurrentChunk()
    {
        final int x = (int) transform.getWorldPosition().getX() / 1400;
        final int y = (int) transform.getWorldPosition().getY() / 800;
        if (chunkX != x || chunkY != y)
        {
            if (currentChunk != null)
            {
                currentChunk.remove(this);
            }
            chunkX = GMath.clamp(x, 0, numberOfChunks - 1);
            chunkY = GMath.clamp(y, 0, numberOfChunks - 1);
            currentChunk = scene.getChunk(chunkX, chunkY);
            currentChunk.add(this);
        }
    }

    public float getLifetime()
    {
        return lifetime;
    }

    /**
     * Sets the lifetime of this entity. Only LIVE_FOREVER or values greater
     * than zero are permitted.
     */
    public void setLifetime(final float lifetime)
    {
        if (lifetime >= 0 || lifetime == GameEntity.LIVE_FOREVER)
        {
            this.lifetime = lifetime;
        }
    }

    /**
     * Adds the lifetime to this entity. If the lifetime counts below zero this
     * entity gets destroyed and aging is stopped.
     */
    public final void addLifetime(final float lifetime)
    {
        if (this.lifetime == GameEntity.LIVE_FOREVER)
        {
            return;
        }
        this.lifetime += lifetime;
        if (getLifetime() <= 0)
        {
            this.lifetime = GameEntity.LIVE_FOREVER;
            destroy();
        }
    }

    public final boolean isAging()
    {
        return lifetime != GameEntity.LIVE_FOREVER;
    }

    @Override
    public void wait(final float seconds)
    {
        deactivate();
        for (final Component component : components)
        {
            component.onDeactivation();
        }
        TimerManager.get().createTimer(seconds, 1, () ->
        {
            activate();
            for (final Component component : components)
            {
                component.onActivation();
            }
        });
    }

    @Override
    public void onUpdate(final float delta)
    {
        if (isAging())
        {
            addLifetime(-delta);
            if (isDestroyed())
            {
                return;
            }
        }

        for (final ExecutionPayload payload : executionList.getIterable())
        {
            if (payload.tick(delta))
            {
                executionList.remove(payload);
            }
        }

        for (final Component component : components)
        {
            component.onUpdate(delta);
        }
    }

    @Override
    public void construct()
    {
        //
    }

    @Override
    protected void onDestroy()
    {
        scene.removeEntity(this);
    }

    @Override
    public String toString()
    {
        String result = super.toString() + ":Position(" + getXPosition() + "," + getYPosition() + ")";
        result += "Components:" + Arrays.toString(components.toArray());
        return result;
    }

    @Override
    public void begin()
    {
        //
    }

    public Transform getTransform()
    {
        return transform;
    }
}
