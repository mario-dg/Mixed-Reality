/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise6.avatar;

import com.jme3.anim.AnimComposer;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;

/**
 * Container for an animated mesh in the jMonkey scene graph.
 */
public class AnimatedMesh extends Node {

  /**
   * Scene graph node.
   */
  private Node node;

  /**
   * This controller is used to define the movement of the mesh over time.
   */
  private AvatarController avatarController;

  public AnimatedMesh(Node node, AvatarController animationController) {
    this.node = node;
    this.avatarController = animationController;
    AnimComposer composer = node.getControl(AnimComposer.class);
    var clips = composer.getAnimClips();
    composer.setCurrentAction("walk");
  }

  /**
   * Call this method to advance in the animation based on the current
   * controller.
   */
  public void update(float time) {
    avatarController.move(time);
    Vector3f p = avatarController.getPose().getPosition();
    getNode().setLocalTranslation(p);
    getNode().setLocalRotation(avatarController.getPose().getRotation());
  }

  // +++ GETTER/SETTER ++++++++++++++++

  public Vector3f getDirection() {
    return avatarController.getDirection();
  }

  public Node getNode() {
    return node;
  }

  public void setAvatarController(AvatarController avatarController) {
    this.avatarController = avatarController;
  }
}
