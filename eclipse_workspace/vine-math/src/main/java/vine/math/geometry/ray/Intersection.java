package vine.math.geometry.ray;

import vine.math.Vec2f;
import vine.math.geometry.Shape;

public class Intersection
{
    boolean  hit;
    float    directionMultiplier;
    Ray      ray;
    Shape    shape;
    Vec2f point;
    Vec2f secondHit;

    /**
     * Initializes the intersection fields.
     */
    public Intersection(boolean hit, float directionMultiplier, Ray ray, Shape shape)
    {
        super();
        this.hit = hit;
        this.directionMultiplier = directionMultiplier;
        this.ray = ray;
        this.shape = shape;
    }

    public boolean isHit()
    {
        return hit;
    }

    public float getDirectionMultiplier()
    {
        return directionMultiplier;
    }

    public Ray getRay()
    {
        return ray;
    }

    public Shape getShape()
    {
        return shape;
    }

}
