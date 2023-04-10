/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise2;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import math.MathF;
import math.Vectors;
import mixedreality.base.math.Utils;
import sprites.Constants;

/**
 * Information about the avatar in the game.
 */
public class Avatar {
    /**
     * Current position of the avatar
     */
    protected Vector2f pos;

    /**
     * Current rotation angle of the avatar (radian measure)
     */
    protected float rotationAngle;

    /**
     * The avatar must be rotated that its orientation matches this vector.
     */
    protected Vector2f targetPos;

    /**
     * Change rate of the orientation per time step.
     */
    public static final float ROTATION_VELOCITY = 0.1f;

    /**
     * Change rate of the position per time step.
     */
    public static final float MOVE_VELOCITY = 0.01f;

    public Avatar() {
        this(new Vector2f(0, 0), 0);
        targetPos = null;
    }

    public Avatar(Vector2f pos, float rotationAngle) {
        this.pos = pos;
        this.rotationAngle = rotationAngle;
        targetPos = null;
    }

    public Vector2f getPos() {
        // Pos = translation part of pose matrix
        return Vectors.xy(makePose().mult(Vector3f.UNIT_Z));
    }

    public Vector2f getOrientation() {
        // Orientation of first colum of rotation part of pose matrix.
        return Vectors.xy(makePose().mult(Vector3f.UNIT_X));
    }

    public void setTargetPos(Vector2f o) {
        this.targetPos = o;
    }

    // ++++++++++++++++ YOUR TASKS START HERE +++++++++++++++++++++++++++++++++

    /**
     * Generate a 3x3 homogeneous transformation matrix which contains the
     * current rotation and p
     */
    protected Matrix3f makePose() {
        var rotMat = Utils.rotationMatrix2D(this.rotationAngle);
        var transMat = Utils.translationMatrix2D(this.pos);
        return transMat.mult(rotMat);
    }

    /**
     * Move the avatar along the current orientation.
     */
    public void moveToTargetPos() {
        if (this.targetPos != null) {
            var currPos = getPos();
            var a = getOrientation().normalize();
            var b = targetPos.normalize();

            var alpha = MathF.atan2(a.x * b.y - a.y * b.x, a.x * b.x + a.y * b.y);
            this.rotationAngle += Math.min(alpha, ROTATION_VELOCITY);

            // Only move, if angle between character orientation and target orientation <= Pi/2
            if (Math.abs(alpha) <= MathF.HALF_PI) {
                var targetVec = this.targetPos.subtract(currPos);

                // stop moving, if the distance between character and target pos < 2f * MOVE_VELOCITY
                this.pos = currPos.add(getOrientation().mult(MOVE_VELOCITY));
                if (targetVec.length() < 2f * MOVE_VELOCITY) {
                    targetPos = null;
                }
            }

        }
    }
}
