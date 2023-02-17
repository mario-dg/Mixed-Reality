/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise4;

import com.jme3.math.Vector4f;

/**
 * An edge connects two points in a polygon.
 *
 * @author Philipp Jenke
 */
public class PolygonEdge {

    /**
     * Start vertex of the edge.
     */
    private PolygonVertex startVertex;

    /**
     * End vertex of the edge.
     */
    private PolygonVertex endVertex;

    /**
     * Color used for debugging.
     */
    private Vector4f color;

    public PolygonEdge(PolygonVertex start, PolygonVertex end) {
        color = new Vector4f(0, 0, 0, 1);
        this.startVertex = start;
        this.endVertex = end;
        start.setOutgoingEdge(this);
        end.setIncomingEdge(this);
    }

    public PolygonVertex getStartVertex() {
        return startVertex;
    }

    public PolygonVertex getEndVertex() {
        return endVertex;
    }

    public Vector4f getColor() {
        return color;
    }

    public void setColor(Vector4f color) {
        this.color = color;
    }

    public void setStartVertex(PolygonVertex startVertex) {
        this.startVertex = startVertex;
    }

    public void setEndVertex(PolygonVertex endVertex) {
        this.endVertex = endVertex;
    }

    @Override
    public String toString() {
        return startVertex.toString() + " -> " + endVertex.toString();
    }
}
