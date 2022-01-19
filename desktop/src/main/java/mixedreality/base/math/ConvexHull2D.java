/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.base.math;

import com.jme3.math.Vector2f;

import java.util.Arrays;

/**
 * Computation of the convex hull of 2D points.
 */
public class ConvexHull2D {

  /**
   * 2D cross product.
   */
  public static double cross(Vector2f O, Vector2f A, Vector2f B) {
    return (A.x - O.x) * (B.y - O.y) - (A.y - O.y) * (B.x - O.x);
  }

  /**
   * Returns a sorted array of points in the convex hull.
   */
  public static Vector2f[] convex_hull(Vector2f[] P) {
    if (P.length > 1) {
      int n = P.length, k = 0;
      Vector2f[] H = new Vector2f[2 * n];

      Arrays.sort(P, (a, b) -> {
        if (a.x == b.x) {
          return (a.y - b.y < 0) ? -1 : 1;
        } else {
          return (a.x - b.x < 0) ? -1 : 1;
        }
      });

      // Build lower hull
      for (int i = 0; i < n; ++i) {
        while (k >= 2 && cross(H[k - 2], H[k - 1], P[i]) <= 0)
          k--;
        H[k++] = P[i];
      }

      // Build upper hull
      for (int i = n - 2, t = k + 1; i >= 0; i--) {
        while (k >= t && cross(H[k - 2], H[k - 1], P[i]) <= 0)
          k--;
        H[k++] = P[i];
      }
      if (k > 1) {
        H = Arrays.copyOfRange(H, 0, k - 1); // remove non-hull vertices after k; remove k - 1 which is a duplicate
      }
      return H;
    } else if (P.length <= 1) {
      return P;
    } else {
      return null;
    }
  }
}
