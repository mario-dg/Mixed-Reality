/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 *
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.base.ui;

import com.jme3.asset.AssetManager;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.ViewPort;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Node;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This scene class is used to represent the scene conent.
 */
public abstract class Scene3D {

  /**
   * This list of tasks is scheduled to be invoked by the JME thread.
   */
  private List<Runnable> runLaterTasks;

  public Scene3D() {
    runLaterTasks = new ArrayList<>();
  }

  /**
   * The init method is called once at the beginning of the runtime.
   */
  public abstract void init(AssetManager assetManager, Node rootNode,
                            CameraController cameraController);

  /**
   * The update method is used to update the simulation state.
   */
  public abstract void update(float time);

  /**
   * This method is called once if the scene is rerendered.
   */
  public abstract void render();

  /**
   * The light setup is done in this method. It is called once at the beginning.
   */
  public void setupLights(AssetManager assetManager, Node rootNode,
                          ViewPort viewPort) {
    // Scene
    PointLight light1 = new PointLight(new Vector3f(-10, 10, -10));
    light1.setColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 1));
    rootNode.addLight(light1);

    // Shadows
    DirectionalLight sun = new DirectionalLight();
    sun.setColor(new ColorRGBA(0.5f, 0.5f, 0.5f, 1));
    sun.setDirection(new Vector3f(0.5f, -1, -0.5f));
    rootNode.addLight(sun);
  }

  /**
   * Load an animated character node.
   *
   * @return Scene graph node with the animated character
   */
  protected Node loadCharacter(AssetManager assetManager, Node rootNode,
                               String gltfFilename) {
    Node node = (Node) assetManager.loadModel(gltfFilename);
    node = (Node) node.getChild("knight");
    node.setShadowMode(RenderQueue.ShadowMode.Cast);
    rootNode.attachChild(node);
    return node;
  }

  /**
   * Return the (descriptive) title of the scene.
   */
  public abstract String getTitle();

  /**
   * Returns the user interface for the scene
   *
   * @return null, if the scene has no user interface
   */
  public abstract JPanel getUI();

  /**
   * Enqueue a task to the list which will be processed when the jMonkey Thread is active.
   */
  protected void runLater(Runnable task) {
    runLaterTasks.add(task);
  }

  public synchronized void invokeRunlaterTasks() {
    runLaterTasks.forEach(task -> task.run());
    runLaterTasks.clear();
  }
}
