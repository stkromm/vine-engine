package vine.animation;

import java.util.Arrays;

import vine.math.GMath;

public class AnimationClip
{
    private final AnimationFrame[] frames;
    private final float            duration;

    /**
     * Creates a AnimationClip from the given AnimationFrames. The given
     * AnimationFrames must be distinct.
     */
    public AnimationClip(final AnimationFrame... frames)
    {
        super();
        Arrays.sort(frames, (o1, o2) -> o1.getTimepoint() >= o2.getTimepoint() ? 1 : -1);
        this.frames = frames;
        duration = frames[frames.length - 1].getTimepoint();
    }

    /**
     * Returns the frame, that corresponds to the given timepoint.
     * <p>
     * The given timepoint is clamp to the interval [0,duration]
     * </p>
     */
    public final AnimationFrame getFrame(final float time)
    {
        final float clampedTime = GMath.clamp(time, 0, duration);
        int frameId = 0;
        while (frames[frameId].getTimepoint() < clampedTime)
        {
            ++frameId;
        }
        return frames[frameId];
    }

    public final float getDuration()
    {
        return duration;
    }
}
