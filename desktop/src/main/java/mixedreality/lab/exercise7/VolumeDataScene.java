/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise7;

import com.jme3.asset.AssetManager;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import misc.AxisAlignedBoundingBox;
import mixedreality.base.mesh.TriangleMesh;
import mixedreality.base.mesh.TriangleMeshTools;
import mixedreality.lab.exercise7.volumeviz.VolumeData;
import mixedreality.lab.exercise7.volumeviz.VolumeDatasetMetaInformation;
import ui.AbstractCameraController;
import ui.Scene3D;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

import static mixedreality.lab.exercise7.volumeviz.VolumeDatasetMetaInformation.DatasetId.NEGHIP;

/**
 * This scene handles and displays volume datasets.
 */
public class VolumeDataScene extends Scene3D {

  /**
   * Current dataset
   */
  protected VolumeData data;

  /**
   * Current VolumeData.Orientation based on camera
   */
  protected VolumeData.Orientation currentOrientation;

  protected AssetManager assetManager;
  protected Node rootNode;
  protected AbstractCameraController cameraController;

  /**
   * In this list, a node with slice quads for each available direction is provided.
   */
  protected List<Node> nodes;

  @Override
  public void init(AssetManager assetManager, Node rootNode, AbstractCameraController cameraController) {
    this.assetManager = assetManager;
    this.rootNode = rootNode;
    this.cameraController = cameraController;
    data = new VolumeData();
    currentOrientation = VolumeData.Orientation.X_NEG;
    nodes = new ArrayList<>();

    // Load a dataset and generate the required textures
    loadDataset(NEGHIP);

    // Set the camera correctly
    AxisAlignedBoundingBox bbox = new AxisAlignedBoundingBox();
    bbox.add(new Vector3f(0, 0, 0));
    bbox.add(new Vector3f(1, 1, 1));
    cameraController.adjustViewTo(bbox);

    // Select the best slice node for the current orientation
    determineOrientation();

    // Add a coordinate system
    makeCoordinateSystem();
  }

  /**
   * Find the current optimal VolumeData.Orientation for the stack
   */
  protected void determineOrientation() {
    // TODO: find optimal orientation.
    VolumeData.Orientation bestOrientation = VolumeData.Orientation.X_NEG;

    if (bestOrientation != currentOrientation) {
      System.out.println("Changed VolumeData.Orientation from " + currentOrientation + " to " + bestOrientation);
      currentOrientation = bestOrientation;

      runLater(() -> {
        rootNode.detachAllChildren();
        rootNode.attachChild(nodes.get(currentOrientation.ordinal()));
        makeCoordinateSystem();
      });
    }
  }

  /**
   * Generate the six texture stacks
   */
  protected void makeSliceStacks() {
    nodes.clear();
    for (VolumeData.Orientation orientation : VolumeData.Orientation.values()) {
      var textures = data.getTextureStack(orientation);
      int numSlices = textures.size();
      Node sliceStackNode = new Node();

      // TODO: Generate slice geometries with the given textures.

      nodes.add(sliceStackNode);
    }
  }

  /**
   * Generate arrows to visualize the coordinate system and add into scene graph
   */
  protected void makeCoordinateSystem() {
    TriangleMesh x = TriangleMeshTools.makeArrow(new Vector3f(0, 0, 0), new Vector3f(1, 0, 0), ColorRGBA.Red);
    TriangleMesh y = TriangleMeshTools.makeArrow(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0), ColorRGBA.Green);
    TriangleMesh z = TriangleMeshTools.makeArrow(new Vector3f(0, 0, 0), new Vector3f(0, 0, 1), ColorRGBA.Blue);
    TriangleMeshTools.unite(x, y);
    TriangleMeshTools.unite(x, z);
    rootNode.attachChild(TriangleMeshTools.createJMonkeyMesh(assetManager, x));
  }

  @Override
  public JPanel getUserInterface() {
    JPanel panel = new JPanel();
    JComboBox<String> comboBoxDataset = new JComboBox<>();
    for (var datasetId : VolumeDatasetMetaInformation.DatasetId.values()) {
      comboBoxDataset.addItem(datasetId.toString());
    }
    comboBoxDataset.addActionListener(e -> {
      loadDataset(VolumeDatasetMetaInformation.DatasetId.valueOf(comboBoxDataset.getSelectedItem().toString()));
      // Force redraw
      currentOrientation = null;
      determineOrientation();
    });
    panel.add(comboBoxDataset);
    return panel;
  }

  /**
   * Load the currently selected dataset.
   */
  private void loadDataset(VolumeDatasetMetaInformation.DatasetId id) {
    data.loadVoxFromFile(id);
    makeSliceStacks();
  }

  @Override
  public void update(float time) {
    determineOrientation();
  }

  @Override
  public void render() {
  }

  @Override
  public String getTitle() {
    return "Volume Visualization";
  }
}
