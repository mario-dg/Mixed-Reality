/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise7.functions;

import com.jme3.math.Vector3f;
import math.MathF;

/**
 * Implicit function of a 3-dimensional torus.
 *
 * @author Philipp Jenke
 */
public class Torus implements ImplicitFunction {

    /**
     * Outer radius of the torus.
     */
    private float radiusOuter;

    /**
     * Inner radius of the torus.
     */
    private float radiusInner;

    public Torus(float radiusOuter, float radiusInner) {
        this.radiusOuter = radiusOuter;
        this.radiusInner = radiusInner;
    }

    @Override
    public float eval(Vector3f p) {
        return MathF.pow((MathF.pow(p.x, 2) + MathF.pow(p.y, 2) + MathF.pow(p.z, 2)
                + MathF.pow(radiusOuter, 2) - MathF.pow(radiusInner, 2)), 2)
                - 4.0f
                * MathF.pow(radiusOuter, 2)
                * (MathF.pow(p.x, 2) + MathF.pow(p.y, 2));
    }
}
