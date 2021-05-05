/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise6.avatar;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector3f;
import mixedreality.lab.exercise6.map.HexMap;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a path for the avatar
 */
public class Path {
  /**
   * Points on the path
   */
  private List<Vector3f> waypoints;

  /**
   * Tangents for the waypoints.
   */
  private List<Vector3f> tangents;

  /**
   * Current parameter on path.
   */
  private float t;

  /**
   * Used for smooth interpolation.
   */
  private Hermite hermite;

  /**
   * Current pose based on the waypoints and t.
   */
  private Pose pose;

  public Path(Vector3f startPos) {
    this.t = 0;
    this.pose = new Pose(startPos, Matrix3f.IDENTITY);
    this.hermite = new Hermite();
    waypoints = new ArrayList<>();
    this.tangents = new ArrayList<>();
  }

  /**
   * Setup path for new list of waypoints.
   */
  public void setup(List<Vector3f> waypoints) {
    t = 0;

    // Waypoints
    this.waypoints.clear();
    this.waypoints.add(0, getCurrentPose().getPosition());
    for (int i = 1; i < waypoints.size(); i++) {
      this.waypoints.add(waypoints.get(i));
    }

    // Tangents
    float tangentScale = HexMap.CELL_SIDELENGTH * 2f;
    tangents.clear();
    tangents.add(getCurrentPose().getDirection().normalize().mult(tangentScale));
    for (int i = 1; i < this.waypoints.size(); i++) {
      int a = Math.max(i - 1, 0);
      int b = Math.min(i + 1, this.waypoints.size() - 1);
      Vector3f start = this.waypoints.get(a);
      Vector3f end = this.waypoints.get(b);
      Vector3f tangent = end.subtract(start).normalize().mult(tangentScale);
      tangents.add(tangent);
    }
  }

  /**
   * Move along the path
   */
  public void update(float speed) {
    if (waypoints == null || waypoints.size() < 2) {
      return;
    }

    t += speed / waypoints.size();
    t = Math.min(t, 1);
    float delta = 1.0f / (waypoints.size() - 1);
    int segmentIndex = (int) (t / delta);
    float alpha = (t - segmentIndex * delta) / delta;
    int segmentIndexPlus = Math.min(segmentIndex + 1, waypoints.size() - 1);
    Vector3f c0 = waypoints.get(segmentIndex);
    Vector3f c1 = waypoints.get(segmentIndexPlus);
    Vector3f m0 = tangents.get(segmentIndex);
    Vector3f m1 = tangents.get(segmentIndexPlus);

    // Hermite
    hermite.setup(c0, m0, m1, c1);
    Vector3f pos = hermite.eval(alpha);
    Vector3f dir = hermite.evalTangent(alpha);
    pose.setPosition(pos);
    pose.setRotationFromZAxis(dir);
  }

  /**
   * Get the current position on the path.
   */
  public Pose getCurrentPose() {
    return pose;
  }
}
