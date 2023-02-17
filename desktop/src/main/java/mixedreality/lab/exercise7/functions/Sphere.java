/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise7.functions;

import com.jme3.math.Vector3f;
import mixedreality.lab.exercise7.functions.ImplicitFunction;

/**
 * Implicit function of a 3-dimensional sphere.
 *
 * @author Philipp Jenke
 */
public class Sphere implements ImplicitFunction {

  /**
   * Radius of the sphere.
   */
  private float radius;

  private Vector3f center;

  /**
   * Constructor.
   *
   * @param radius Initial value for the radius.
   */
  public Sphere(float radius, Vector3f center) {
    this.radius = radius;
    this.center = center;
  }

  /*
   * (nicht-Javadoc)
   *
   * @see
   * edu.cg1.exercises.marchingcubes.ImplicitFunction3D#f(javax.vecmath.Point3d
   * )
   */
  @Override
  public float eval(Vector3f p) {
    return p.distance(center) - radius;
  }

}
