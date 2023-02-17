/**
 * Prof. Philipp Jenke
 * Hochschule für Angewandte Wissenschaften (HAW), Hamburg
 * Lecture demo program.
 */
package mixedreality.lab.exercise7.functions;

import com.jme3.math.Vector3f;

/**
 * Parent interface for implicit functions.
 * 
 * @author Philipp Jenke
 * 
 */
public interface ImplicitFunction {

    /**
     * Evaluates the implicit function a given spatial location.
     * 
     * @param p
     *            The function is evaluated at this point.
     * @return Function value.
     */
    float eval(Vector3f p);
}
