package vine.gameplay.entity;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import vine.math.Matrix3f;
import vine.math.Matrix4f;
import vine.math.Vector2f;
import vine.math.Vector3f;

/**
 * @author Steffen
 *
 */
public final class Transform {
    /*
     * If the parent is null, then the transform is the world transform.
     */
    private Transform parent = null;
    private final List<Transform> children = new ArrayList<>();

    private float rotation = 0;
    private final Vector2f scale = new Vector2f(1, 1);
    private final Vector2f position = new Vector2f(0, 0);
    private float worldRotation = 0;
    private final Vector2f worldScale = new Vector2f(1, 1);
    private final Vector2f worldPosition = new Vector2f(0, 0);
    private boolean isDirty = false;
    @SuppressWarnings("unused")
    private Matrix3f localToWorldMatrix = Matrix3f.getIdentity(); // NOSONAR
    private boolean isInverseDirty = false;
    private Matrix3f worldToLocalMatrix = Matrix3f.getIdentity();
    private Matrix4f localToWorldMatrixUsed;

    /**
     * 
     */
    public Transform() {
        super();
    }

    private void setDirty() {
        if (!isDirty) {
            isDirty = true;
            isInverseDirty = true;
            for (Transform transform : children) {
                transform.setDirty();
            }
        }
    }

    /**
     * @return The current angles of rotation of this transform
     */
    public float getRotation() {
        return rotation;
    }

    /**
     * @return The current scale of this transform
     */
    public Vector2f getScale() {
        return scale;
    }

    /**
     * @return The current position of this transform
     */
    public Vector2f getPosition() {
        return position;
    }

    /**
     * @param rotation
     *            The new rotation in angles of this transform
     */
    public void setRotation(float rotation) {
        this.rotation = rotation;
        setDirty();
    }

    /**
     * Sets the scale of this transformation.
     * 
     * @param x
     *            The new x scale of this transform
     * @param y
     *            The new y scale of this transform
     */
    public void setScale(float x, float y) {
        this.scale.setX(x);
        this.scale.setY(y);
        setDirty();
    }

    /**
     * Sets the translation of this transformation.
     * 
     * @param x
     *            The new x position of this transfrom
     * @param y
     *            The new y position of this transform
     */
    public void setPosition(float x, float y) {
        this.position.setX(x);
        this.position.setY(y);
        setDirty();
    }

    /**
     * Reparents this transformation. The localToWorld Transformation is then
     * the composed transformation of this transform and the parent localToWorld
     * transformation.
     * 
     * @param transform
     *            The transform that should be parent of this transform
     */
    public void setParent(Transform transform) {
        Transform tempTrans = transform;
        while (tempTrans != null) {
            if (tempTrans.equals(this)) {
                return;
            }
            tempTrans = tempTrans.parent;
        }
        if (parent != null) {
            parent.children.remove(this);
        }
        parent = transform;
        if (parent != null) {
            parent.children.add(this);
        }
        assert parent.checkTransformStructure();
        setDirty();
    }

    private void updateLocalToWorldTransformation() {
        Deque<Transform> parentStack = new ArrayDeque<>();
        Transform temp = this;
        while (temp != null) {
            parentStack.push(temp);
            temp = temp.parent;
        }
        temp = parentStack.pop();
        float posX = temp.position.getX();
        float posY = temp.position.getY();
        temp.worldPosition.setX(posX);
        temp.worldPosition.setY(posY);
        float scaleX = temp.scale.getX();
        float scaleY = temp.scale.getY();
        temp.worldScale.setX(scaleX);
        temp.worldScale.setY(scaleY);
        float rotat = temp.rotation;
        temp.worldRotation = rotat;
        while (!parentStack.isEmpty()) {
            temp = parentStack.pop();
            posX += temp.position.getX();
            posY += temp.position.getY();
            scaleX *= temp.scale.getX();
            scaleY *= temp.scale.getY();
            rotat += temp.rotation;
            temp.worldPosition.setX(posX);
            temp.worldPosition.setY(posY);
            temp.worldScale.setX(scaleX);
            temp.worldScale.setY(scaleY);
            temp.worldRotation = rotat;
            temp.localToWorldMatrix = Matrix3f.getIdentity().translate(worldPosition.getX(), worldPosition.getY())
                    .scale(worldScale.getX(), worldScale.getY()).rotate(worldRotation);
            temp.isDirty = false;
        }
    }

    /**
     * Transform, that transforms from the local space of this transform to
     * world space. E.g used to render components with this transform.
     * 
     * @return the local to world transformation
     */
    public Matrix4f getLocalToWorldTransformation() {
        if (isDirty) {
            updateLocalToWorldTransformation();
            this.localToWorldMatrixUsed = Matrix4f
                    .translate(new Vector3f(this.worldPosition.getX(), this.worldPosition.getY(), 0));
        }
        return this.localToWorldMatrixUsed;
    }

    /**
     * Transformation, that can be used to transform directions and points in
     * the local space of this transform.
     * 
     * @return The world to local transformation
     */
    public Matrix3f getWorldToLocalTransformation() {
        if (isInverseDirty) {
            getLocalToWorldTransformation();
            worldToLocalMatrix = Matrix3f.getIdentity();
            worldToLocalMatrix.translate(-worldPosition.getX(), -worldPosition.getY());
            worldToLocalMatrix.scale(1 / worldScale.getX(), 1 / worldScale.getY());
            worldToLocalMatrix.setA12(worldToLocalMatrix.getA12() / worldScale.getX());
            worldToLocalMatrix.setA13(worldToLocalMatrix.getA13() / worldScale.getX());
            worldToLocalMatrix.setA21(worldToLocalMatrix.getA21() / worldScale.getY());
            worldToLocalMatrix.setA23(worldToLocalMatrix.getA23() / worldScale.getY());
            worldToLocalMatrix.rotate(-worldRotation);
            isInverseDirty = false;
        }
        return worldToLocalMatrix;
    }

    /**
     * Auxiliary function, that checks, if the parent structure is valid. For
     * testing purpose.
     * 
     * @return True, if the transform structure is valid.
     */
    public boolean checkTransformStructure() {
        for (Transform child : children) {
            if (!child.parent.equals(this)) {
                return false;
            }
        }
        return true;
    }
}
