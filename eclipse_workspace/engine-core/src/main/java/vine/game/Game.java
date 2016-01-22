package vine.game;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import vine.event.EventDispatcher;
import vine.game.GameObject.ReferenceManager;
import vine.game.screen.GameScreen;
import vine.game.screen.Screen;
import vine.gameplay.component.Camera;
import vine.gameplay.component.Sprite;
import vine.gameplay.entity.GameEntity;
import vine.gameplay.entity.PlayerPawn;
import vine.graphics.Graphics;
import vine.graphics.SceneCreationException;
import vine.graphics.Scene;
import vine.settings.GameSettings;
import vine.window.Window;

/**
 * Manages the gameplay on a global level. That is managing level changer
 * persistent objects and resources.
 * 
 * * Use the instantiate methods to create new objects of GameObject derived
 * classes.
 * 
 * You can add a name parameter to the instantiate method. Use this to declare a
 * name identifier for the new gameobject you can later use, to access the
 * gameobject from other objects in the game. You might check, that the given
 * name parameter is not used for a gameobject already though or the
 * instantiation of the gameobject will fail.
 * 
 * @author Steffen
 *
 */
public class Game {
    private volatile Scene scene;
    private volatile Screen screen;
    private volatile Graphics graphics;
    private static Game runningGame;

    private Game() {

    }

    /**
     * @return The current running game.
     */
    public static Game getGame() {
        if (runningGame == null) {
            return null;
        }
        return runningGame;
    }

    /**
     * @return The graphics provide used by the game.
     */
    public Graphics getGraphics() {
        return graphics;
    }

    /**
     * @return Returns the games viewport.
     */
    public final Screen getScreen() {
        return screen;
    }

    /**
     * @return The current scene of the game.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * @param delta
     *            The time that passed since the last update
     */
    static void update(float delta) {
        runningGame.scene.update(delta);
    }

    /**
     * @param level
     *            The asset name of the level that should be loaded.
     */
    public static void changeLevel(String level) {
        try {
            runningGame.scene = Scene.SceneBuilder.createScene(level);
        } catch (SceneCreationException e) {
            Logger.getGlobal().log(Level.SEVERE, "Failed to create scene " + level, e);
        }
        EventDispatcher.registerListener(runningGame.scene);
        for (int i = 0; i < 500; i++) {
            GameEntity entity = Game.instantiate(GameEntity.class);
            Sprite sprite = Game.instantiate(Sprite.class);
            entity.addComponent(sprite);
            runningGame.scene.getEntities().add(entity);
            entity.setScene(runningGame.scene);
        }
        PlayerPawn entity = Game.instantiate(PlayerPawn.class, new Integer(1), new Integer(2));
        Sprite sprite = Game.instantiate(Sprite.class);
        entity.addComponent(sprite);
        Camera camera = runningGame.scene.cameras.instantiateCamera();
        entity.addComponent(camera);
        runningGame.scene.cameras.activate(camera);
        runningGame.scene.getEntities().add(entity);
        entity.setScene(runningGame.scene);
    }

    /**
     * 
     */
    static void render() {
        runningGame.scene.render();
    }

    /**
     * Creates a new World and destroys all objects of the old world.
     * 
     * @param window
     *            the window the game runs in
     * @param graphics
     *            The graphics provider used to render the game
     */
    static void init(Window window, Graphics graphics) {
        runningGame = new Game();
        runningGame.graphics = graphics;
        runningGame.screen = new GameScreen(window, 9.f / 16, 720);
        getObjectsByType(GameObject.class).stream().forEach(e -> e.destroy());
        changeLevel(GameSettings.getStartLevelName());
    }

    /**
     * @param type
     *            Class, that is instantiated
     * @param params
     *            The optinal arguments of the construct method of the
     *            instantiated type.
     * @return Returns all GameObjects in the Game of the given type.
     */
    public static final <T extends GameObject> T instantiate(Class<T> type, Object... params) {
        return ReferenceManager.instantiate(type, ReferenceManager.generateObjectName(type), params);
    }

    /**
     * @param type
     *            The class type of the new object. Has to be a subclass of
     *            Gameobject.
     * @param name
     *            The name of the object. The gameobject can be found by this
     *            name throughout the game.
     * @param params
     *            The optinal arguments of the construct method of the
     *            instantiated type.
     * @return the newly created gameobject
     */
    public static final <T extends GameObject> T instantiate(Class<T> type, String name, Object... params) {
        if (name.contains("?")) {
            return null;
        }
        if (!ReferenceManager.objects.containsKey(name)) {
            return ReferenceManager.instantiate(type, name, params);
        }
        return null;
    }

    /**
     * @param type
     *            Class that is used to look for objects of.
     * @return A stream object with all gameobject of the given type.
     */
    public static final <T extends GameObject> List<T> getObjectsByType(Class<T> type) {
        List<T> list = new ArrayList<>();
        ReferenceManager.objects.values().stream().forEach(obj -> {
            if (obj.getClass() == type) {
                list.add(type.cast(obj));
            }
        });
        return list;
    }

    /**
     * @param name
     *            Name identifier that is look up for a object.
     * @return The objects, that exists in the system by the given name.
     */
    public static final GameObject getObjectByName(String name) {
        return ReferenceManager.objects.get(name);
    }
}
