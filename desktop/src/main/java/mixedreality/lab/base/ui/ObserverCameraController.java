/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */
package mixedreality.lab.base.ui;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.control.CameraControl;

/**
 * Control the camera movement.
 */
public class ObserverCameraController extends AbstractCameraController {

  /**
   * Rotation angle offset when rotating around the up-vector.
   */
  private final float ANGLE_ROUND_UP = 0.06f;

  /**
   * Rotation speed.
   */
  private final float ROTATE_SPEED = 0.3f;

  /**
   * Reference point.
   */
  private Vector3f ref;

  public ObserverCameraController(Camera cam) {
    super(cam);
    ref = Vector3f.UNIT_X;
  }

  @Override
  public void setup(Vector3f eye, Vector3f ref, Vector3f up) {
    super.setup(eye, ref, up);
    this.ref = new Vector3f(ref);
  }

  @Override
  public void rotateAroundUp(float sign) {
    Vector3f eye = getCamera().getLocation();
    Vector3f dir = ref.subtract(eye);
    Matrix3f m = new Matrix3f();
    m.fromAngleAxis(ANGLE_ROUND_UP * sign * ROTATE_SPEED, getCamera().getUp());
    Vector3f newDir = m.mult(dir);
    Vector3f newEye = ref.subtract(newDir);
    getCamera().setLocation(newEye);
    getCamera().lookAt(ref, getCamera().getUp());
  }

  @Override
  public void rotateAroundLeft(float sign) {
    Vector3f eye = getCamera().getLocation();
    Vector3f dir = ref.subtract(eye);
    Matrix3f m = new Matrix3f();
    m.fromAngleAxis(ANGLE_ROUND_UP * sign * ROTATE_SPEED, getCamera().getLeft());
    Vector3f newDir = m.mult(dir);
    Vector3f newEye = ref.subtract(newDir);
    getCamera().setLocation(newEye);
    getCamera().lookAt(ref, getCamera().getUp());
  }

  @Override
  public void lookAt(Vector3f ref, Vector3f up) {
    this.ref = new Vector3f(ref);
    getCamera().lookAt(ref, up);
  }

  @Override
  public void zoom(float delta) {
    Vector3f eye = getCamera().getLocation();
    Vector3f ref = this.ref;
    Vector3f dir = ref.subtract(eye);
    dir = dir.mult(delta / 500.0f);
    getCamera().setLocation(eye.add(dir));
  }

  @Override
  public void update() {
  }
}
