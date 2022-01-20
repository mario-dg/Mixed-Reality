/**
 * This file is part of the computer graphics project of the computer graphics group led by
 * Prof. Dr. Philipp Jenke at the University of Applied Sciences (HAW) in Hamburg.
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
