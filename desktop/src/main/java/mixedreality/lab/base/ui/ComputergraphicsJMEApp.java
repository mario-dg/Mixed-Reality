/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 *
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
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;

/**
 * This is the main application which is used for all exercises. Only adjust the
 * scene in the main method.
 */
public class ComputergraphicsJMEApp extends SimpleApplication {

  public static final String MOUSE_MOVE_RIGHT = "MOUSE_MOVE_RIGHT";
  public static final String MOUSE_MOVE_LEFT = "MOUSE_MOVE_LEFT";
  public static final String MOUSE_MOVE_UP = "MOUSE_MOVE_UP";
  public static final String MOUSE_MOVE_DOWN = "MOUSE_MOVE_DOWN";
  public static final String MOUSE_LEFT_BUTTON = "MOUSE_LEFT_BUTTON";
  public static final String MOUSE_RIGHT_BUTTON = "MOUSE_RIGHT_BUTTON";
  /**
   * Camera controller.
   */
  private CameraController cameraController;

  /**
   * Scene providing the content.
   */
  private final Scene3D scene;

  /**
   * Mouse click state
   */
  private boolean mouseLeftPressed, mouseRightPressed, mouseMiddlePressed;

  public ComputergraphicsJMEApp(Scene3D scene) {
    this.scene = scene;
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
    cameraController = new CameraController(cam);
    cameraController.lookAt(new Vector3f(0, 1, 0), new Vector3f(0, 1, 0));
    viewPort.setBackgroundColor(ColorRGBA.LightGray);
    inputManager.addMapping(MOUSE_MOVE_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
    inputManager.addMapping(MOUSE_MOVE_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
    inputManager.addMapping(MOUSE_MOVE_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, true));
    inputManager.addMapping(MOUSE_MOVE_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
    inputManager.addMapping(MOUSE_LEFT_BUTTON, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    inputManager.addMapping(MOUSE_RIGHT_BUTTON, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));
    inputManager.addListener(analogListener, new String[]{
            MOUSE_MOVE_RIGHT, MOUSE_MOVE_LEFT, MOUSE_MOVE_UP, MOUSE_MOVE_DOWN});
    inputManager.addListener(actionListener, new String[]{MOUSE_LEFT_BUTTON, MOUSE_RIGHT_BUTTON});
  }

  /**
   * This listener is used for discrete input events.
   */
  private ActionListener actionListener = (name, keyPressed, tpf) -> {
    switch (name) {
      case MOUSE_LEFT_BUTTON:
        mouseLeftPressed = keyPressed;
        return;
      case MOUSE_RIGHT_BUTTON:
        mouseRightPressed = keyPressed;
        return;
    }
  };

  /**
   * This listener is used for continuous input events.
   */
  private AnalogListener analogListener =(name, value, tpf) -> {
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
}