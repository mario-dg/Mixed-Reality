/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.base.math;

import com.jme3.math.Vector2f;

/**
 * A bounding box for 2D objects
 */
public class BoundingBox2D {
  /**
   * Corner points
   */
  private Vector2f ll, ur;

  public BoundingBox2D() {
    ll = ur = null;
  }

  /**
   * Insert point into bounding box - update boundaries
   * @param p
   */
  public void addPoint(Vector2f p) {
    if (ll == null) {
      ll = new Vector2f(p);
      ur = new Vector2f(p);
    } else {
      if (p.x < ll.x) {
        ll.x = p.x;
      }
      if (p.x > ur.x) {
        ur.x = p.x;
      }
      if (p.y < ll.y) {
        ll.y = p.y;
      }
      if (p.y > ur.y) {
        ur.y = p.y;
      }
    }
  }

  public Vector2f getLL() {
    return ll;
  }

  public Vector2f getUR() {
    return ur;
  }
}
