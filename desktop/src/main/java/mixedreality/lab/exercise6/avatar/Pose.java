/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise6.avatar;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;

/**
 * The pose encodes the current position an rotation of the thing
 */
public class Pose {
  /*
   * Position in 3-space.
   */
  private Vector3f pos;

  /**
   * Rotation matrix
   */
  private Matrix3f rot;

  public Pose(Vector3f pos, Matrix3f rot) {
    this.pos = pos;
    this.rot = rot;
  }

  public Vector3f getPosition() {
    return pos;
  }

  public Matrix3f getRotation() {
    return rot;
  }

  public Vector3f getDirection() {
    return getRotation().mult(Vector3f.UNIT_Z);
  }

  public void setPosition(Vector3f pos) {
    this.pos = pos;
  }

  /**
   * Create the rotation matrix from a direction.
   */
  public void setRotationFromZAxis(Vector3f z) {
    z = z.normalize();
    Vector3f y = new Vector3f(0, 1, 0);
    Vector3f x = y.cross(z).normalize();
    rot.fromAxes(x, y, z);
  }
}
