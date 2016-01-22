package vine.gameplay.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import vine.game.GameObject;
import vine.gameplay.component.Component;
import vine.gameplay.component.Sprite;
import vine.graphics.Scene;
import vine.math.Vector2f;
import vine.math.Vector3f;

/**
 * @author Steffen
 *
 */
public class GameEntity extends GameObject { // NOSONAR
    protected volatile Vector2f velocity = new Vector2f(0, 0);
    protected volatile Vector3f position = new Vector3f((int) (Math.random() * 1000), (int) (Math.random() * 500),
            0.2f);
    private Scene scene = null;
    private Group group = null;
    private final List<String> tags = new ArrayList<>();
    private final List<Sprite> sprites = new ArrayList<>();

    /**
     * 
     */
    protected Transform transform = new Transform();
    /**
     * 
     */
    private List<Component> components = new ArrayList<>();

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    public float getZ() {
        return position.getZ();
    }

    /**
     * @return All Sprites that this entity contains.
     */
    public Collection<Sprite> getSprites() {
        return sprites;
    }

    /**
     * @return The scene this entity is currently present in
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * @return The group this entity is a member of
     */
    public Group getGroup() {
        return group;
    }

    /**
     * @param scene
     *            Sets the scene of this entity
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * @param group
     *            Sets the group of this entity
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        components.stream().forEach(c -> c.update(delta));
    }

    @Override
    public void begin() {
        components.stream().forEach(c -> c.begin());
    }

    @Override
    protected void construct() {
        //
    }

    /**
     * @param tag
     *            The tag that is looked for
     * @return true, if this entity contains the given tag
     */
    public boolean containsTag(String tag) {
        return tags.contains(tag);
    }

    /**
     * @param tag
     *            The tag that should be added to this object
     */
    public void addTag(String tag) {
        tags.add(tag);
    }

    /**
     * @param tag
     *            The tag should be removed from this object
     */
    public void removeTag(String tag) {
        tags.remove(tag);
    }

    /**
     * @param type
     *            The type by which components are searched
     * @return all components found of this type
     */
    @SuppressWarnings("unchecked")
    public <T extends Component> Collection<T> getComponents(Class<T> type) {
        return (Collection<T>) components.stream().filter(c -> c.getClass() == type);
    }

    /**
     * @param component
     *            The component, that should be attached to this entity
     */
    public final void addComponent(Component component) {
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
    public final void removeComponent(Component component) {
        components.removeIf(c -> c.getName().equals(component.getName()));
    }

    /**
     * @param type
     *            The type of searched component
     * @return the reference to a component, that is of the given type or null
     *         if there is non
     */
    public <T extends Component> T getComponent(Class<T> type) {
        Optional<Component> opt = components.stream().filter(c -> c.getClass() == type).findFirst();
        if (opt.isPresent()) {
            return (T) opt.get();
        }
        return null;
    }
}
