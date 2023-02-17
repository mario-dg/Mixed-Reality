/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise7.functions;

import com.jme3.math.Vector3f;
import math.MathF;
import mixedreality.lab.exercise7.functions.ImplicitFunction;

/**
 * Implementation of the 3D GourSat function (https://de.wikipedia.org/wiki/%C3%89douard_Goursat).
 */
public class GourSat implements ImplicitFunction {

    /**
     * Function parameters.
     */
    private float a, b, c;

    /**
     * Constructor.
     */
    public GourSat() {
        a = 0.0f;
        b = -5.0f;
        c = 11.8f;
    }

    @Override
    public float eval(Vector3f p) {
        double scale = 3;
        double x = p.x * scale;
        double y = p.y * scale;
        double z = p.z * scale;
        return (MathF.pow(x, 4) + MathF.pow(y, 4) + MathF.pow(z, 4)
                + a * MathF.pow((Math.pow(x, 2) + MathF.pow(y, 2) + MathF.pow(z, 2)), 2)
                + b * (MathF.pow(x, 2) + MathF.pow(y, 2) + MathF.pow(z, 2)) + c);
    }
}
