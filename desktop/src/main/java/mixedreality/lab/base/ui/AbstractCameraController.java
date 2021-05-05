package mixedreality.lab.base.ui;

import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.control.CameraControl;

/**
 * Base class for all camera controllers.
 */
public abstract class AbstractCameraController extends CameraControl {

  public AbstractCameraController(Camera cam) {
    super(cam);
    float fovy = 90;
    float aspect = (float) cam.getWidth() / (float) cam.getHeight();
    float near = 0.1f;
    float far = 20;
    cam.setFrustumPerspective(fovy, aspect, near, far);
  }

  /**
   * Set the camera coordinate system using eye, ref, up
   */
  public void setup(Vector3f eye, Vector3f ref, Vector3f up) {
    getCamera().setLocation(eye);
    getCamera().lookAt(ref, up);
  }

  /**
   * Rotate the camera around the up vector.
   */
  public abstract void rotateAroundUp(float sign);

  /**
   * Rotate the camera around the left vector.
   */
  public abstract void rotateAroundLeft(float sign);

  /**
   * Set a new look position (reference and up vector).
   */
  public abstract void lookAt(Vector3f ref, Vector3f up);

  /**
   * Zoom closer to the reference point.
   */
  public abstract void zoom(float delta);

  /**
   * This method is called on each rendering update.
   */
  public abstract void update();
}
