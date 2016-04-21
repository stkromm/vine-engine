package vine.gameplay.entity;

import org.lwjgl.glfw.GLFW;

import vine.animation.AnimationStateManager;
import vine.assets.AssetManager;
import vine.event.KeyEvent;
import vine.game.scene.GameEntity;
import vine.gameplay.component.AnimatedSprite;
import vine.graphics.Color;
import vine.input.InputAction;
import vine.sound.AudioPlayer;
import vine.sound.SoundClip;

/**
 * @author Steffen
 *
 */
public class PlayerPawn extends GameEntity
{
    AnimationStateManager animation;
    AudioPlayer           player = new AudioPlayer();

    @Override
    public void onUpdate(final float delta)
    {
        super.onUpdate(delta * 1);
    }

    @Override
    public void begin()
    {
        final AnimatedSprite sprite = this.getComponent(AnimatedSprite.class);
        this.animation = sprite.getAnimationManager();
    }

    private void onMoveButtonReleased(final int button)
    {
        switch (button) {
        case GLFW.GLFW_KEY_W:
            this.setSpeedY(this.getYSpeed() > -64 ? this.getYSpeed() - 64 : -64);
        break;
        case GLFW.GLFW_KEY_A:
            this.setSpeedX(this.getXSpeed() < 64 ? this.getXSpeed() + 64 : 64);
        break;
        case GLFW.GLFW_KEY_D:
            this.setSpeedX(this.getXSpeed() > -64 ? this.getXSpeed() - 64 : -64);
        break;
        case GLFW.GLFW_KEY_S:
            this.setSpeedY(this.getYSpeed() < 64 ? this.getYSpeed() + 64 : 64);
        break;
        default:
        break;
        }
        this.setAnimationState();
    }

    private void setAnimationState()
    {
        if (this.getXSpeed() == 0 && this.getYSpeed() == 0)
        {
            switch (this.animation.getCurrentState().getName()) {
            case "down":
                this.animation.changeState("idle-down");
            break;
            case "up":
                this.animation.changeState("idle-up");
            break;
            case "left":
                this.animation.changeState("idle-left");
            break;
            case "right":
                this.animation.changeState("idle-right");
            break;
            default:
            }

        } else if (this.getXSpeed() > 0)
        {
            this.animation.changeState("right");
        } else if (this.getXSpeed() < 0)
        {
            this.animation.changeState("left");
        } else if (this.getYSpeed() < 0)
        {
            this.animation.changeState("down");
        } else if (this.getYSpeed() > 0)
        {
            this.animation.changeState("up");
        }
    }

    private void onMoveButtonPressed(final int button)
    {
        switch (button) {
        case GLFW.GLFW_KEY_A:
            this.setSpeedX(this.getXSpeed() > 64 ? 64 : this.getXSpeed() - 64);
        break;
        case GLFW.GLFW_KEY_D:
            this.setSpeedX(this.getXSpeed() < -64 ? -64 : this.getXSpeed() + 64);
        break;
        case GLFW.GLFW_KEY_S:
            this.setSpeedY(this.getYSpeed() < -64 ? -64 : this.getYSpeed() - 64);
        break;
        case GLFW.GLFW_KEY_W:
            this.setSpeedY(this.getYSpeed() > 64 ? 64 : this.getYSpeed() + 64);
        break;
        case GLFW.GLFW_KEY_F:
            this.flash(new Color(256, 256, 0, 0), 2, false);
            this.wait(5.f);
        break;
        default:
        break;
        }
        this.setAnimationState();
    }

    @Override
    public boolean onKeyEvent(final KeyEvent keyEvent)
    {
        if (keyEvent.getAction() == InputAction.RELEASED)
        {
            this.onMoveButtonReleased(keyEvent.getKey());
        } else if (keyEvent.getAction() == InputAction.PRESS)
        {
            this.onMoveButtonPressed(keyEvent.getKey());
        }
        return true;
    }

    @Override
    public void construct()
    {
        this.setAcceleration(0, 0);
        this.player.setClip(AssetManager.loadSync("music", SoundClip.class));
        // this.player.playLooped();
    }
}
