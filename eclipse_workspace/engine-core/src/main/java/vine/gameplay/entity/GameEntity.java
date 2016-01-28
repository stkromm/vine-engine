package vine.gameplay.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import vine.game.GameObject;
import vine.gameplay.component.Component;
import vine.gameplay.component.Sprite;
import vine.gameplay.scene.Scene;
import vine.math.Vector2f;
import vine.math.Vector3f;

/**
 * @author Steffen
 *
 */
public class GameEntity extends GameObject { // NOSONAR
    /**
     * 
     */
    protected final Vector2f velocity = new Vector2f(0, 0);
    /**
     * 
     */
    protected final Vector3f position = new Vector3f((int) (Math.random() * 1000), (int) (Math.random() * 500), 0.2f);
    private Scene scene;
    private Group group;
    private final List<String> tags = new ArrayList<>();
    private final List<Sprite> sprites = new ArrayList<>();

    /**
     * 
     */
    protected final Transform transform = new Transform();
    /**
     * 
     */
    private final List<Component> components = new ArrayList<>();

    /**
     * @return
     */
    public float getX() {
        return position.getX();
    }

    /**
     * @return
     */
    public float getY() {
        return position.getY();
    }

    /**
     * @return
     */
    public float getZ() {
        return position.getZ();
    }

    /**
     * @return All Sprites that this entity contains.
     */
    public final Collection<Sprite> getSprites() {
        return sprites;
    }

    /**
     * @return The scene this entity is currently present in
     */
    public final Scene getScene() {
        return scene;
    }

    /**
     * @return The group this entity is a member of
     */
    public final Group getGroup() {
        return group;
    }

    /**
     * @param scene
     *            Sets the scene of this entity
     */
    public final void setScene(final Scene scene) {
        this.scene = scene;
    }

    /**
     * @param group
     *            Sets the group of this entity
     */
    public final void setGroup(final Group group) {
        this.group = group;
    }

    @Override
    public void update(final float delta) {
        super.update(delta);
        components.stream().forEach(c -> c.update(delta));
    }

    @Override
    public void begin() {
        components.stream().forEach(c -> c.begin());
    }

    /**
     * @param tag
     *            The tag that is looked for
     * @return true, if this entity contains the given tag
     */
    public final boolean containsTag(final String tag) {
        return tags.contains(tag);
    }

    /**
     * @param tag
     *            The tag that should be added to this object
     */
    public final void addTag(final String tag) {
        tags.add(tag);
    }

    /**
     * @param tag
     *            The tag should be removed from this object
     */
    public final void removeTag(final String tag) {
        tags.remove(tag);
    }

    /**
     * @param type
     *            The type by which components are searched
     * @return all components found of this type
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> Collection<T> getComponents(final Class<T> type) {
        return (Collection<T>) components.stream().filter(c -> c.getClass() == type);
    }

    /**
     * @param component
     *            The component, that should be attached to this entity
     */
    public final void addComponent(final Component component) {
        removeComponent(component);
        if (component instanceof Sprite) {
            sprites.add((Sprite) component);
        }
        components.add(component);
        component.attachTo(this);
    }

    /**
     * @param component
     *            The component that should be removed from this entity
     */
    public final void removeComponent(final Component component) {
        components.removeIf(c -> c.getName().equals(component.getName()));
    }

    /**
     * @param type
     *            The type of searched component
     * @return the reference to a component, that is of the given type or null
     *         if there is non
     */
    public final <T extends Component> Optional<T> getComponent(final Class<T> type) {
        return (Optional<T>) components.stream().filter(c -> c.getClass() == type).findFirst();
    }

    /**
     * @param x
     * @param y
     */
    public void construct(final int x, final int y) {
        this.position.setX(x);
        this.position.setY(y);
        this.position.setZ(0);
    }
}