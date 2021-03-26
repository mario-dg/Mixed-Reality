/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise2;

import com.jme3.math.Matrix4f;

/**
 * This model contains information about the state of the plane.
 */
public class PlaneModel {

  public PlaneModel() {
  }

  /**
   * This transformation encodes the complete pose of the plane and is used for rendering.
   */
  public Matrix4f getPose() {

    Matrix4f pose = new Matrix4f();

    // Scaling matrix
    float scaleFactor = 1.0f;
    Matrix4f scale = new Matrix4f(
            scaleFactor, 0, 0, 0,
            0, scaleFactor, 0, 0,
            0, 0, scaleFactor, 0,
            0, 0, 0, 1
    );
    pose = scale.mult(pose);

    // TODO: provide correct matrix here (rotation and translation)

    return pose;
  }

  /**
   * Update the position of the plane
   *
   * @param time Time since last call of this method.
   */
  public void update(float time) {
    // TODO
  }
}
