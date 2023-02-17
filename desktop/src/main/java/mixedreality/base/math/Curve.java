/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */
package mixedreality.base.math;

import com.jme3.math.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * A curve with control points.
 */
public class Curve {

    /**
     * Control points
     */
    private List<Vector2f> controlPoints = new ArrayList<Vector2f>();

    private BasisFunction basisFunction;

    public Curve(BasisFunction basisFunction) {
        this.basisFunction = basisFunction;
    }

    public Vector2f eval(float t) {
        if (controlPoints.size() == 0) {
            throw new IllegalArgumentException("Invalid control point list.");
        }
        Vector2f p = new Vector2f();
        for (int i = 0; i <= getDegree(); i++) {
            p = p.add(controlPoints.get(i).mult(basisFunction.eval(t, i, getDegree())));
        }
        return p;
    }

    public Vector2f evalDerivative(float t) {
        if (controlPoints.size() == 0) {
            throw new IllegalArgumentException("Invalid control point list.");
        }
        Vector2f tangent = new Vector2f();
        for (int i = 0; i <= getDegree(); i++) {
            tangent = tangent.add
                    (controlPoints.get(i).mult(basisFunction.evalDerivative(t, i, getDegree())));
        }
        return tangent;
    }

    public Vector2f getControlPoint(int i) {
        return controlPoints.get(i);
    }

    public int getDegree() {
        return controlPoints.size() - 1;
    }

    public void addControlPoint(Vector2f p) {
        controlPoints.add(p);
    }

    public void removeControlPoint(int currentControlPointIndex) {
        controlPoints.remove(currentControlPointIndex);
    }

    public BasisFunction getBasisFunction() {
        return basisFunction;
    }

    public void setControlPoints(Vector2f... controlPoints) {
        this.controlPoints.clear();
        for (Vector2f controlPoint : controlPoints) {
            this.controlPoints.add(controlPoint);
        }

    }

    public void setBasisFunction(BasisFunction basisFunction) {
        this.basisFunction = basisFunction;
    }
}
