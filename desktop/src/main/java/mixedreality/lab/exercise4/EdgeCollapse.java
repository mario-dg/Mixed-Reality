/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise4;

import com.jme3.math.Matrix3f;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

/**
 * Internal datastructure to represent an edge collapse operation.
 *
 * @author Philipp Jenke
 */
public class EdgeCollapse {
    public EdgeCollapse(double error, Matrix3f Q, Vector2f newPos) {
        this.error = error;
        this.Q = Q;
        this.newPos = newPos;
    }

    /**
     * Error (estimate) of the collapse
     */
    public double error;

    /**
     * Quadric error metric
     */
    public Matrix3f Q;

    /**
     * Optimal position after the collapse
     */
    public Vector2f newPos;

    @Override
    public String toString() {
        return "v_new:" + newPos + " (error: " + String.format("%.2f", error) + ")\n" + Q.toString();
    }
}