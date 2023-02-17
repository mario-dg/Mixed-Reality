/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise4;

import com.jme3.math.Vector2f;
import misc.Logger;
import mixedreality.base.math.BoundingBox2D;

import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Representation of a polygon in 3-space.
 *
 * @author Philipp Jenke
 */
public class Polygon {

  public enum Type {
    OPEN, CLOSED
  }

  private List<PolygonVertex> points;
  private List<PolygonEdge> edges;
  private Type type;

  public Polygon() {
    points = new LinkedList<>();
    edges = new LinkedList<>();
    type = Type.CLOSED;
  }

  public Polygon(Polygon polygon) {
    points = new LinkedList<>();
    Map<PolygonVertex, PolygonVertex> vertexMap = new HashMap<>();
    for (PolygonVertex v : polygon.points) {
      PolygonVertex newP = new PolygonVertex(v);
      points.add(newP);
      vertexMap.put(v, newP);
    }
    edges = new LinkedList<>();
    for (PolygonEdge e : polygon.edges) {
      edges.add(new PolygonEdge(
              vertexMap.get(e.getStartVertex()),
              vertexMap.get(e.getEndVertex())));
    }
    type = polygon.type;
  }

  public BoundingBox2D getBBox() {
    BoundingBox2D bbox = new BoundingBox2D();
    for (PolygonVertex p : points) {
      bbox.addPoint(p.getPosition());
    }
    return bbox;
  }

  public void addPoint(PolygonVertex p) {
    points.add(p);
  }

  public int getNumPoints() {
    return points.size();
  }

  public PolygonVertex getPoint(int index) {
    return points.get(index);
  }

  public void clear() {
    points.clear();
    edges.clear();
  }

  /**
   * Collapse edge, remove edge from list, remove edge-end from list. Returns
   * the remaining point.
   */
  public PolygonVertex collapse(PolygonEdge polygonEdge, Vector2f newPosition) {
    PolygonVertex start = polygonEdge.getStartVertex();
    PolygonVertex end = polygonEdge.getEndVertex();
    edges.remove(polygonEdge);
    if (!points.remove(end)) {
      Logger.getInstance().error("Failed to remove point.");
    }

    start.getPosition().set(newPosition);
    start.setOutgoingEdge(end.getOutgoingEdge());
    if (end.getOutgoingEdge() != null) {
      end.getOutgoingEdge().setStartVertex(start);
    }
    return start;
  }


  /**
   * Copy points
   */
  public void copy(Polygon other) {
    clear();
    for (int i = 0; i < other.points.size(); i++) {
      addPoint(new PolygonVertex(other.points.get(i).getPosition()));
    }
    for (int edgeIndex = 0; edgeIndex < other.edges.size(); edgeIndex++) {
      PolygonEdge edge = other.edges.get(edgeIndex);
      int startIndex = other.points.indexOf(edge.getStartVertex());
      int endIndex = other.points.indexOf(edge.getEndVertex());
      addEdge(startIndex, endIndex);
    }
  }

  public Type getType() {
    return type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public boolean isClosed() {
    return type == Type.CLOSED;
  }

  public void addPoint(Vector2f position) {
    addPoint(new PolygonVertex(position));
  }

  public int getNumEdges() {
    return edges.size();
  }

  public PolygonEdge getEdge(int edgeIndex) {
    return edges.get(edgeIndex);
  }

  public void addEdge(int startIndex, int endIndex) {
    PolygonVertex p0 = points.get(startIndex);
    PolygonVertex p1 = points.get(endIndex);
    PolygonEdge edge = new PolygonEdge(p0, p1);
    p0.setOutgoingEdge(edge);
    p1.setIncomingEdge(edge);
    edges.add(edge);
  }

  /**
   * Insert new point in middle of edge
   */
  public void splitEdge(PolygonEdge edge) {
    PolygonVertex p = edge.getStartVertex();
    PolygonVertex q = edge.getEndVertex();
    PolygonVertex newVertex = new PolygonVertex((p.getPosition().add(q.getPosition())).mult(0.5f));
    edge.setEndVertex(newVertex);
    newVertex.setIncomingEdge(edge);
    PolygonEdge newEdge = new PolygonEdge(newVertex, q);
    points.add(newVertex);
    edges.add(newEdge);
  }

  public void readFromFile(String filename) {
    clear();
    String strLine = "";
    try {
      InputStream is = getInputStream(filename);
      if (is == null) {
        Logger.getInstance().error("Failed to create input stream for file " + filename);
        return;
      }

      // Get the object of DataInputStream
      DataInputStream in = new DataInputStream(is);
      BufferedReader br = new BufferedReader(new InputStreamReader(in));

      // Read File Line By Line
      while ((strLine = br.readLine()) != null) {
        // Print the content on the console
        Vector2f point = parseLine(strLine);
        if (point != null) {
          addPoint(point);
          if (getNumPoints() > 1) {
            addEdge(getNumPoints() - 2, getNumPoints() - 1);
          }
        }
      }

      // Closed polygons
      addEdge(getNumPoints() - 1, 0);
    } catch (IOException e) {
      Logger.getInstance().error("Failed to parse polygon file.");
    }

    Logger.getInstance().msg("Successfully read polygon with " + getNumPoints() + " points and "
            + getNumEdges() + " edges.");
  }

  /**
   * Read line, convert to (2D) point.
   */
  private Vector2f parseLine(String line) {
    String[] tokens = line.trim().split("\\s+");
    if (tokens.length != 3) {
      return null;
    }
    try {
      float x = Float.valueOf(tokens[0].replace(',', '.'));
      float y = Float.valueOf(tokens[1].replace(',', '.'));
      //float z = Float.valueOf(tokens[2].replace(',', '.'));
      return new Vector2f(x, y);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  /**
   * Get in input stream from a file.
   */
  private InputStream getInputStream(String filename) {
    File initialFile = new File("src/main/resources/" + filename);
    try {
      InputStream stream = new FileInputStream(initialFile);
      if (stream == null) {
        Logger.getInstance().msg(
                "Mesh file " + filename + " cannot be found.");
      }
      return stream;
    } catch (FileNotFoundException e) {
      Logger.getInstance().error("Failed to read mesh file " + filename);
      return null;
    }
  }
}
