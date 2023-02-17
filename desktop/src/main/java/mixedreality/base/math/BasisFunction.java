/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.base.math;

/**
 * Shared interface for all basis functions.
 */
public interface BasisFunction {
    /**
     * Evaluate basis function.
     */
    float eval(float t, int index, int degree);

    /**
     * Evaluate basis function derivative.
     */
    float evalDerivative(float t, int i, int degree);
}
