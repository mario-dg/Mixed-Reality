/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise4;

import com.jme3.math.Vector2f;

/**
 * A polygon vertex can have up to two edges (incoming, outgoing).
 *
 * @author Philipp Jenke
 */
public class PolygonVertex {

    /**
     * Point position.
     */
    private final Vector2f position;

    /**
     * Incoming edge.
     */
    private PolygonEdge incomingEdge;

    /**
     * Outgoing edge.
     */
    private PolygonEdge outgoingEdge;

    public PolygonVertex(Vector2f position) {
        this.position = position;
        incomingEdge = null;
        outgoingEdge = null;
    }

    public PolygonVertex(PolygonVertex polygon) {
        this.position = new Vector2f(polygon.position);
        incomingEdge = null;
        outgoingEdge = null;
    }

    public PolygonEdge getIncomingEdge() {
        return incomingEdge;
    }

    public void setIncomingEdge(PolygonEdge incomingEdge) {
        this.incomingEdge = incomingEdge;
    }

    public PolygonEdge getOutgoingEdge() {
        return outgoingEdge;
    }

    public void setOutgoingEdge(PolygonEdge outgoingEdge) {
        this.outgoingEdge = outgoingEdge;
    }

    public Vector2f getPosition() {
        return position;
    }

    @Override
    public String toString() {
        return position.toString();
    }
}
