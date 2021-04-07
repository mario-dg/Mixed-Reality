package mixedreality.lab.exercise3;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;

/**
 * Represents a virtual camera with extrinsic and intrinsic camera parameters
 */
public class Camera {

  /**
   * Camera eye point
   */
  private Vector3f eye;

  /**
   * Reference point to look to
   */
  private Vector3f ref;

  /**
   * Camera up vector
   */
  private Vector3f up;

  /**
   * Field of view along the horizontal axis.
   */
  private float fovx;

  public Camera(Vector3f eye, Vector3f ref, Vector3f up, float fovx) {
    this.eye = eye;
    this.ref = ref;
    this.up = up;
    this.fovx = fovx;
  }

  public Vector3f getEye() {
    return eye;
  }

  public Vector3f getRef() {
    return ref;
  }

  public Vector3f getUp() {
    return up;
  }

  public float getFovx() {
    return fovx;
  }

  /**
   * Rotate the eye horizontally around the up-vector centered at the reference point.
   */
  public void rotateHorizontal(float dx) {
    eye = eye.subtract(ref);
    float angle = dx * 0.01f;
    Matrix3f R = new Matrix3f();
    R.fromAngleAxis(angle, up);
    eye = R.mult(eye);
    eye = eye.add(ref);
  }

  /**
   * Rotate the eye vertically around the left vector centered at the reference point.
   */
  public void rotateVertical(float dy) {
    Vector3f left = ref.subtract(eye).cross(up).normalize();
    eye = eye.subtract(ref);
    float angle = dy * 0.01f;
    Matrix3f R = new Matrix3f();
    R.fromAngleAxis(angle, left);
    eye = R.mult(eye);
    eye = eye.add(ref);
    up = left.cross(ref.subtract(eye.normalize()));
  }
}
