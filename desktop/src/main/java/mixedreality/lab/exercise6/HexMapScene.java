/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise6;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.queue.RenderQueue;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import misc.Logger;
import mixedreality.base.mesh.ObjReader;
import mixedreality.base.mesh.Triangle;
import mixedreality.base.mesh.TriangleMesh;
import mixedreality.base.mesh.TriangleMeshTools;
import mixedreality.lab.exercise6.avatar.AnimatedMesh;
import mixedreality.lab.exercise6.avatar.AvatarController;
import mixedreality.lab.exercise6.map.Cell;
import mixedreality.lab.exercise6.map.HexMap;
import ui.AbstractCameraController;
import ui.ObserverCameraController;
import ui.Scene3D;
import java.util.Iterator;

/**
 * Base 3D scene for exercise 6.
 */
public class HexMapScene extends Scene3D {

  public enum CameraControllerType {
    OVERVIEW, FIRST_PERSON, OVER_SHOULDER
  }

  /**
   * This is the (hexagonal) map of the scene.
   */
  protected HexMap map;

  /**
   * Cached tree mesh.
   */
  protected TriangleMesh treeMeshCache;

  /**
   * This is the representation of the avatar
   */
  protected AnimatedMesh avatar;

  /**
   * This controller is used to control the avatar movement (along a path)
   */
  protected AvatarController avatarController;

  /**
   * 2D canvas for the map.
   */
  private HexMapView2D hexMapView2D;

  /**
   * Currently set camera controller.
   */
  protected AbstractCameraController currentCameraController;

  /**
   * This node is the parent node of all cell nodes.
   */
  protected Node allCellNodes;

  private AssetManager assetManager;

  public HexMapScene() {
    map = new HexMap(13, 8);
    avatar = null;
    ObjReader reader = new ObjReader();
    treeMeshCache = reader.read("Models/tree.obj");
    hexMapView2D = new HexMapView2D(200, 300, map);
    avatarController = null;
    allCellNodes = null;
  }

  @Override
  public void init(AssetManager assetManager, Node rootNode, AbstractCameraController cameraController) {
    this.currentCameraController = cameraController;
    this.assetManager = assetManager;
    setCameraController(CameraControllerType.OVERVIEW);
    makeNodesForMap(assetManager, rootNode);

    runLater(() -> {
      Node knightNode = loadCharacter(assetManager, rootNode,
              "Models/knight.gltf");
      knightNode.setLocalScale(0.005f * HexMap.CELL_SIDELENGTH);
      avatarController = new AvatarController(
              HexMap.to3D(map.getRandomNonOccupedCell().getGlobalCenter()), 1f, map);
      avatar = new AnimatedMesh(knightNode, avatarController);
      hexMapView2D.setAnimationController(avatarController);
    });
  }

  @Override
  public void update(float time) {
    avatar.update(time);
    hexMapView2D.update();
  }

  @Override
  public void render() {
  }

  @Override
  public String getTitle() {
    return "Hex Map";
  }

  /**
   * Generate the jMonkey geometry and nodes for the map.
   */
  protected void makeNodesForMap(AssetManager assetManager, Node rootNode) {
    allCellNodes = new Node();
    Node allTreeNodes = new Node();
    for (Iterator<Cell> cellIt = map.getCellIterator(); cellIt.hasNext(); ) {
      Cell cell = cellIt.next();
      TriangleMesh cellMesh = makeMeshForCell(cell);
      Geometry geometry = TriangleMeshTools.createJMonkeyMesh(assetManager, cellMesh);
      geometry.setShadowMode(RenderQueue.ShadowMode.Receive);
      allCellNodes.attachChild(geometry);

      if (cell.getContent() == Cell.Content.TREE) {
        Geometry treeGeometry = TriangleMeshTools.createJMonkeyMesh(assetManager, treeMeshCache);
        treeGeometry.setLocalTranslation(HexMap.to3D(cell.getGlobalCenter()));
        treeGeometry.setLocalScale(HexMap.CELL_SIDELENGTH * 0.07f);
        treeGeometry.setShadowMode(RenderQueue.ShadowMode.Cast);
        allTreeNodes.attachChild(treeGeometry);
      }
    }
    rootNode.attachChild(allCellNodes);
    rootNode.attachChild(allTreeNodes);
  }

  /**
   * Generate a triangle mesh for a cell.
   */
  protected TriangleMesh makeMeshForCell(Cell cell) {
    Vector2f center = cell.getGlobalCenter();
    TriangleMesh mesh = new TriangleMesh();
    for (int i = 0; i < 6; i++) {
      Vector2f localCorner = cell.getLocalCorner(i);
      Vector2f t = (localCorner.add(new Vector2f(1, 1))).mult(0.5f);
      Vector2f globalCorner = cell.getGlobalCorner(i);
      mesh.addVertex(HexMap.to3D(globalCorner));
      mesh.addTextureCoordinate(t);
    }
    mesh.addVertex(HexMap.to3D(center));
    mesh.addTextureCoordinate(new Vector2f(0.5f, 0.5f));
    for (int i = 0; i < 6; i++) {
      mesh.addTriangle(new Triangle(6, (i + 1) % 6, i, 6, (i + 1) % 6, i));
    }
    mesh.setColor(ColorRGBA.Orange);
    mesh.computeTriangleNormals();
    switch (cell.getContent()) {
      case TREE -> mesh.setTextureName("Textures/hex_tile_grass.jpg");
      case EMPTY -> mesh.setTextureName("Textures/hex_tile_empty.jpg");
    }
    return mesh;
  }

  /**
   * Load an animated character node.
   *
   * @return Scene graph node with the animated character
   */
  protected Node loadCharacter(AssetManager assetManager, Node rootNode, String gltfFilename) {
    Node node = (Node) assetManager.loadModel(gltfFilename);
    node = (Node) node.getChild("knight");
    node.setShadowMode(RenderQueue.ShadowMode.Cast);
    rootNode.attachChild(node);
    return node;
  }

  /**
   * Set another camera controller.
   */
  protected void setCameraController(CameraControllerType type) {
    Camera camera = currentCameraController.getCamera();
    switch (type) {
      case OVERVIEW -> {
        currentCameraController = new ObserverCameraController(camera);
        currentCameraController.setup(new Vector3f(7, 7, 7), map.getCenter(), Vector3f.UNIT_Y);
      }
      case FIRST_PERSON -> {
        // TODO
        Logger.getInstance().msg("TODO: set first person camera controller.");
      }
      case OVER_SHOULDER -> {
        // TODO
        Logger.getInstance().msg("TODO: set first over-shoulder controller.");
      }
    }
  }

  @Override
  public void handlePicking(Ray pickingRay) {
    // TODO
    Logger.getInstance().msg("TODO: handle picking event.");
  }

  /*
  @Override
  public JPanel getUI() {
    JPanel uiPanel = new JPanel();
    uiPanel.setLayout(new BoxLayout(uiPanel, BoxLayout.Y_AXIS));
    uiPanel.add(hexMapView2D);

    JComboBox<String> comboBoxCameraController = new JComboBox<>();
    for (CameraControllerType type : CameraControllerType.values()) {
      comboBoxCameraController.addItem(type.toString());
    }
    comboBoxCameraController.addActionListener(e -> {
      CameraControllerType type =
              CameraControllerType.valueOf((String) comboBoxCameraController.getSelectedItem());
      setCameraController(type);
    });
    uiPanel.add(comboBoxCameraController);
    return uiPanel;
  }
   */
}
