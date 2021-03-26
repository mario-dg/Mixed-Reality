/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise2;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Matrix4f;
import com.jme3.math.Transform;
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
 * Base 3D scene for exercise 2.
 */
public class TransformationsScene extends Scene3D {

  /**
   * The asset manager is used to read content (e.g. triangle meshes or texture)
   * from file to jMonkey.
   */
  private AssetManager assetManager;

  /**
   * This is the root node of the scene graph with all the scene content.
   */
  private Node rootNode;

  /**
   * Model of the plane.
   */
  protected PlaneModel planeModel;

  /**
   * This is the geometry node for the plane.
   */
  private Geometry planeNode;

  /**
   * UI components
   */
  protected JSlider yawRudder;
  protected JSlider pitchElevator;

  public TransformationsScene() {
    assetManager = null;
    rootNode = null;
    planeModel = new PlaneModel();
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
    TriangleMesh mesh = reader.read("Models/plane.obj");
    planeNode = TriangleMeshTools.createJMonkeyMesh(assetManager, mesh,
            "Textures/plane.png", null);
    planeNode.setShadowMode(RenderQueue.ShadowMode.Cast);
    rootNode.attachChild(planeNode);
  }

  @Override
  public void update(float time) {
    // Update the state of the plane
    planeModel.update(time);
  }

  @Override
  public void render() {
    // Render the plane based transformation created from the current state
    Matrix4f pose = planeModel.getPose();
    Transform t = new Transform();
    t.fromTransformMatrix(pose);
    planeNode.setLocalTransform(t);
  }

  @Override
  public String getTitle() {
    return "Mixed Reality";
  }

  @Override
  public JPanel getUI() {
    JPanel introUi = new JPanel();
    introUi.setLayout(new BoxLayout(introUi, BoxLayout.Y_AXIS));

    // Yaw rudder (left/right)
    JLabel labelYawRudder = new JLabel("Yaw rudder: 0");
    introUi.add(labelYawRudder);
    yawRudder = new JSlider(JSlider.HORIZONTAL, -100, 100, 0);
    yawRudder.addChangeListener(e -> {
      float MAX_RUDDER_VALUE = 10.0f;
      float yawRudderAngle = yawRudder.getValue() / 100.0f * MAX_RUDDER_VALUE;
      labelYawRudder.setText("Pitch elevator: " + String.format("%.1f°", yawRudderAngle));
      //Logger.getInstance().msg("yaw rudder angle: " + yawRudderAngle);
    });
    introUi.add(yawRudder);

    // Pitch elevator (up/down)
    JLabel labelPitchElevator = new JLabel("Pitch elevator: 0");
    introUi.add(labelPitchElevator);
    pitchElevator = new JSlider(JSlider.VERTICAL, -100, 100, 0);
    pitchElevator.addChangeListener(e -> {
      float MAX_RUDDER_VALUE = 10.0f;
      float pitchElevatorAngle = pitchElevator.getValue() / 100.0f * MAX_RUDDER_VALUE;
      labelPitchElevator.setText("Pitch elevator: " + String.format("%.1f°", pitchElevatorAngle));
      //Logger.getInstance().msg("yaw rudder angle: " + pitchElevatorAngle);
    });
    introUi.add(pitchElevator);

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
