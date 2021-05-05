/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.base.ui;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import mixedreality.lab.base.Observable;
import mixedreality.lab.base.Observer;

/**
 * This is the main application which is used for all exercises. Only adjust the
 * scene in the main method.
 */
public class ComputergraphicsJMEApp extends SimpleApplication implements Observer {

  public static final String MOUSE_MOVE_RIGHT = "MOUSE_MOVE_RIGHT";
  public static final String MOUSE_MOVE_LEFT = "MOUSE_MOVE_LEFT";
  public static final String MOUSE_MOVE_UP = "MOUSE_MOVE_UP";
  public static final String MOUSE_MOVE_DOWN = "MOUSE_MOVE_DOWN";
  public static final String MOUSE_LEFT_BUTTON = "MOUSE_LEFT_BUTTON";
  public static final String MOUSE_RIGHT_BUTTON = "MOUSE_RIGHT_BUTTON";
  public static final String PICKING = "PICKING";
  /**
   * Camera controller.
   */
  private AbstractCameraController cameraController;

  /**
   * Scene providing the content.
   */
  private Scene3D scene = null;

  /**
   * Mouse click state
   */
  private boolean mouseLeftPressed, mouseRightPressed, mouseMiddlePressed;

  public ComputergraphicsJMEApp(Scene3D scene) {
    this.scene = scene;
    scene.addObserver(this);
  }

  @Override
  public void simpleInitApp() {
    setupInput();

    // Debug
    setDisplayFps(false);
    setDisplayStatView(false);

    scene.setupLights(assetManager, rootNode, viewPort);
    scene.init(assetManager, rootNode, cameraController);
    viewPort.setBackgroundColor(ColorRGBA.White);
  }

  /**
   * Register the handlers for the mouse and key input (also used for the camera
   * controller).
   */
  private void setupInput() {
    cam.setLocation(new Vector3f(-5, 5, 5));
    stateManager.detach(stateManager.getState(FlyCamAppState.class));
    cameraController = new ObserverCameraController(cam);
    cameraController.lookAt(new Vector3f(0, 1, 0), new Vector3f(0, 1, 0));
    viewPort.setBackgroundColor(ColorRGBA.LightGray);
    inputManager.addMapping(MOUSE_MOVE_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
    inputManager.addMapping(MOUSE_MOVE_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
    inputManager.addMapping(MOUSE_MOVE_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
    inputManager.addMapping(MOUSE_MOVE_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
    inputManager.addMapping(MOUSE_LEFT_BUTTON, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    inputManager.addMapping(MOUSE_RIGHT_BUTTON, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
    inputManager.addMapping(PICKING, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

    inputManager.addListener(analogListener, new String[]{
            MOUSE_MOVE_RIGHT, MOUSE_MOVE_LEFT, MOUSE_MOVE_UP, MOUSE_MOVE_DOWN});
    inputManager.addListener(actionListener, new String[]{MOUSE_LEFT_BUTTON, MOUSE_RIGHT_BUTTON, PICKING});
  }

  /**
   * This listener is used for discrete input events.
   */
  private ActionListener actionListener = (name, keyPressed, tpf) -> {
    switch (name) {
      case MOUSE_LEFT_BUTTON -> {
        mouseLeftPressed = keyPressed;
        return;
      }
      case MOUSE_RIGHT_BUTTON -> {
        mouseRightPressed = keyPressed;
      }
      case PICKING -> {
        if (!keyPressed) {
          Vector2f click2d = inputManager.getCursorPosition().clone();
          Vector3f click3d = cam.getWorldCoordinates(
                  click2d, 0f).clone();
          Vector3f dir = cam.getWorldCoordinates(
                  click2d, 1f).subtractLocal(click3d).normalizeLocal();
          scene.handlePicking(new Ray(cam.getLocation(), dir));
          return;
        }
      }
    }
  };

  /**
   * This listener is used for continuous input events.
   */
  private AnalogListener analogListener = (name, value, tpf) -> {
    float rotateFactor = 200.0f;
    float zoomFactor = 800.0f;
    switch (name) {
      case MOUSE_MOVE_LEFT:
        if (mouseLeftPressed) {
          rotateHorizontal(-rotateFactor * value);
        }
        break;
      case MOUSE_MOVE_RIGHT:
        if (mouseLeftPressed) {
          rotateHorizontal(rotateFactor * value);
        }
        break;
      case MOUSE_MOVE_UP:
        if (mouseLeftPressed) {
          rotateVertical(rotateFactor * value);
        } else if (mouseRightPressed) {
          zoom(zoomFactor * value);
        }
        break;
      case MOUSE_MOVE_DOWN:
        if (mouseLeftPressed) {
          rotateVertical(-rotateFactor * value);
        } else if (mouseRightPressed) {
          zoom(-zoomFactor * value);
        }
        break;
    }
  };

  @Override
  public void simpleUpdate(float tpf) {
    scene.invokeRunlaterTasks();
    scene.update(tpf);
    if (scene.hasNewCameraController()) {
      ObserverCameraController cameraController = scene.getCameraController(cam);
      if (cameraController != null) {
        Logger.getInstance().msg("Switched to camera controller " + cameraController);
        this.cameraController = cameraController;
      }
    }
    cameraController.update();
  }

  @Override
  public void simpleRender(RenderManager rm) {
    scene.render();
  }

  public void zoom(float delta) {
    cameraController.zoom(delta);
  }

  public void rotateHorizontal(float delta) {
    cameraController.rotateAroundUp(delta);
  }

  public void rotateVertical(float delta) {
    cameraController.rotateAroundLeft(delta);
  }

  @Override
  public void update(Observable sender, String descr, Object payload) {
    // Set another camera controller
    if (descr.equals(Scene3D.MSG_SET_CAMERA_CONTROLLER)) {
      AbstractCameraController cameraController = (AbstractCameraController) payload;
      this.cameraController = cameraController;
    }
  }
}