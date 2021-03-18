/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise2;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Box;
import com.jme3.shader.VarType;
import mixedreality.lab.base.mesh.ObjReader;
import mixedreality.lab.base.mesh.TriangleMesh;
import mixedreality.lab.base.mesh.TriangleMeshTools;
import mixedreality.lab.base.ui.CameraController;
import mixedreality.lab.base.ui.Scene3D;

import javax.swing.*;

/**
 * A sample 3D scene which contains a ground plane and a building mesh.
 */
public class IntroScene extends Scene3D {

  /**
   * The asset manager is used to read content (e.g. triangle meshes or texture)
   * from file to jMonkey.
   */
  private AssetManager assetManager;

  /**
   * This is the root node of the scene graph with all the scene content.
   */
  private Node rootNode;

  public IntroScene() {
    assetManager = null;
    rootNode = null;
  }

  @Override
  public void init(AssetManager assetManager, Node rootNode,
                   CameraController cameraController) {
    this.assetManager = assetManager;
    this.rootNode = rootNode;
    cameraController.setup(new Vector3f(-10, 10, -10),
            new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));

    // Add a square to the scene graph
    createGround();

    // Load a building mesh from a file and add it to the scene.
    ObjReader reader = new ObjReader();
    TriangleMesh mesh = reader.read("Models/house.obj");
    Geometry node = TriangleMeshTools.createJMonkeyMesh(assetManager, mesh,
            "Textures/house.png", null);
    node.setShadowMode(RenderQueue.ShadowMode.Cast);
    node.scale(5);
    node.move(new Vector3f(4, 0, 4));
    node.rotate(0, -90, 0);
    rootNode.attachChild(node);
  }

  @Override
  public void update(float time) {
  }

  @Override
  public void render() {
  }

  @Override
  public String getTitle() {
    return "Mixed Reality";
  }

  @Override
  public JPanel getUI() {
    JPanel introUi = new JPanel();
    JLabel label = new JLabel("no user interface");
    introUi.add(label);
    return introUi;
  }

  /**
   * Generate a ground mesh.
   */
  private void createGround() {
    float extend = 15;
    Box box = new Box(new Vector3f(-extend / 2, -0.05f, -extend / 2),
            new Vector3f(extend / 2, 0, extend / 2));
    Geometry quadGeometry = new Geometry("Ground", box);
    Material mat = new Material(assetManager,
            "Common/MatDefs/Light/Lighting.j3md");
    mat.setColor("Diffuse",
            new ColorRGBA(0.3f, 0.15f, 0.05f, 1));
    mat.setParam("UseMaterialColors", VarType.Boolean, true);
    quadGeometry.setMaterial(mat);
    rootNode.attachChild(quadGeometry);
    quadGeometry.setShadowMode(RenderQueue.ShadowMode.Receive);
  }
}
