/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise1;

import mixedreality.lab.base.math.BasisFunction;

/**
 * Dummy implentation for a basis function to be used in a curve.
 */
public class BasisFunctionDummy implements BasisFunction {
  @Override
  public float eval(float t, int index, int degree) {
    return 0;
  }

  @Override
  public float evalDerivative(float t, int i, int degree) {
    return 0;
  }
}
