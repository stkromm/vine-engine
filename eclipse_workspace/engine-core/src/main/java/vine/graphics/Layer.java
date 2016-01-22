package vine.graphics;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import vine.event.Event;
import vine.event.EventDispatcher.EventHandler;
import vine.gameplay.entity.GameEntity;

/**
 * Provides a unique rendering method with post processing, global screnn
 * maniuplation for a set of gameobjects. Offers no semantic ordering of
 * gameobjects and is not relevant for physics or audio.
 * 
 * @author Steffen
 * 
 */
public abstract class Layer {
    /**
     * 
     */
    protected List<GameEntity> entities = new ArrayList<>();
    /**
     * 
     */
    protected Renderer renderer = new Renderer();

    public List<EventHandler> handler = new ArrayList<>();

    public void addKeyHandler(EventHandler handler) {
        this.handler.add(handler);
    }

    public void addMouseButtonHandler(EventHandler handler) {
        this.handler.add(handler);
    }

    public boolean onEvent(Event event) {
        Optional<EventHandler> opt = handler.stream().filter(h -> h.onEvent(event)).findFirst();
        return opt.isPresent();
    }

    /**
     * @return All entities that are rendered by this layer
     */
    public List<GameEntity> getEntities() {
        return entities;
    }

    /**
     * Adds the given entity to this layer.
     * 
     * @param entity
     *            Entity that will be rendered with this layer.
     */
    public void add(GameEntity entity) {
        if (!entities.contains(entity)) {
            entities.add(entity);
        }
    }

    /**
     * Renders all entities assigned to this layer with it renderer.
     */
    public abstract void render();

}
