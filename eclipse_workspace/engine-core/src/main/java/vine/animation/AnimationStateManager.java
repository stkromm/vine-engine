package vine.animation;

import java.util.HashMap;
import java.util.Map;

public class AnimationStateManager {
    private final Map<String, AnimationState> states;
    private AnimationState currentState;
    private float time;

    public AnimationStateManager(final AnimationState[] animation) {
        if (animation.length > 0) {
            currentState = animation[animation.length - 1];
        }

        states = new HashMap<>();
        for (final AnimationState state : animation) {
            states.put(state.getName(), state);
        }
    }

    public final void changeState(final String name) {
        if (states.containsKey(name)) {
            currentState = states.get(name);
        }
    }

    public final void tick(final float delta) {
        this.time += delta * currentState.getPlaybackSpeed();
        this.time = time % currentState.getClip().getDuration();
    }

    public final float[] getCurrentFrame() {
        return currentState.getClip().getFrame(time).getUvs();
    }

    public final AnimationState getCurrentState() {
        return currentState;
    }
}