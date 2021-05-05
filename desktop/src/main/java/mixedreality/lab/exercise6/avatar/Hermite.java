package mixedreality.lab.exercise6.avatar;

import com.jme3.math.Vector3f;

/**
 * Hermite curve of degree 3.
 */
public class Hermite {

  /**
   * Control points
   */
  private Vector3f c0, m0, m1, c1;

  /**
   * Must be called before eval();
   */
  public void setup(Vector3f c0, Vector3f m0, Vector3f m1, Vector3f c1) {
    this.c0 = c0;
    this.m0 = m0;
    this.m1 = m1;
    this.c1 = c1;
  }

  /**
   * Evaluate curve at t. t must be in [0;1]
   */
  public Vector3f eval(float t) {
    return c0.mult(eval(t, 0))
            .add(m0.mult(eval(t, 1)))
            .add(m1.mult(eval(t, 2)))
            .add(c1.mult(eval(t, 3)));
  }

  /**
   * Evaluate tangent at t. t must be in [0;1]
   */
  public Vector3f evalTangent(float t) {
    return c0.mult(evalDerivative(t, 0))
            .add(m0.mult(evalDerivative(t, 1)))
            .add(m1.mult(evalDerivative(t, 2)))
            .add(c1.mult(evalDerivative(t, 3)));
  }

  /**
   * Evaluate basis function index at t.
   */
  private float eval(float t, int index) {
    switch (index) {
      case 0:
        return (1-t);//(1 - t) * (1 - t) * (1 + 2 * t);
      case 1:
        return t * (1 - t) * (1 - t);
      case 2:
        return -t * t * (1 - t);
      case 3:
        return t;//(3 - 2 * t) * t * t;
      default:
        throw new IllegalArgumentException("Invalid Index");
    }
  }

  /**
   * Evaluate derivative of basis function index at t.
   */
  private float evalDerivative(float t, int index) {
    switch (index) {
      case 0:
        return -6 * t + 6 * t * t;
      case 1:
        return 1 - 4 * t + 3 * t * t;
      case 2:
        return -2 * t + 3 * t * t;
      case 3:
        return 6 * t - 6 * t * t;
      default:
        throw new IllegalArgumentException("Invalid Index");
    }
  }
}
