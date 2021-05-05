/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise6.avatar;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import mixedreality.lab.exercise6.map.Cell;
import mixedreality.lab.exercise6.map.HexMap;
import mixedreality.lab.exercise6.shortestPath.Graph;
import mixedreality.lab.exercise6.shortestPath.Node;
import mixedreality.lab.exercise6.shortestPath.ShortestPathDijkstra;

import java.util.Iterator;
import java.util.stream.Collectors;

/**
 * An entity has a location and orientation in the scene.
 */
public class AvatarController {

  /**
   * Path for the entity to move on.
   */
  private Path path;

  /**
   * Movement spee.
   */
  private float speed;

  /**
   * The paths are planned on this map.
   */
  private HexMap hexMap;

  /**
   * Graph of the valid paths in the map.
   */
  private Graph<Cell> graph;

  /**
   * Implementation of dijkstras algorithm to find the shortest path.
   */
  private ShortestPathDijkstra<Cell> shortestPath;

  /**
   * Selected target cell
   */
  private Cell targetCell;

  public AvatarController(Vector3f startPos, float speed, HexMap hexMap) {
    this.path = new Path(startPos);
    this.speed = speed;
    this.hexMap = hexMap;
    this.graph = null;
    buildGraph();
    this.shortestPath = new ShortestPathDijkstra<>();
    this.targetCell = null;
  }

  public Pose getPose() {
    return path.getCurrentPose();
  }

  /**
   * Update pose.
   */
  public void move(float time) {
    path.update(time * speed);
  }

  /**
   * Set a new target cell - plan a path towards it.
   */
  public void setNewTarget(Cell target) {
    this.targetCell = target;
    Vector3f pos3D = getPose().getPosition();
    Cell currentCell = hexMap.getClosestCell(new Vector2f(pos3D.x, pos3D.z));
    shortestPath.setup(graph, graph.getKnoten(currentCell), graph.getKnoten(target));
    shortestPath.compute();
    var waypoints = shortestPath.getPath();
    this.path.setup(waypoints
            .stream()
            .map(c -> HexMap.to3D(c.getElement().getGlobalCenter()))
            .collect(Collectors.toList()));
  }

  /**
   * Build the graph for the hex map.
   */
  private void buildGraph() {
    graph = new Graph<>();
    // Add nodes
    for (Iterator<Cell> cellIt = hexMap.getCellIterator(); cellIt.hasNext(); ) {
      Cell c = cellIt.next();
      if (c.getContent() == Cell.Content.EMPTY) {
        graph.addKnoten(new Node(c));
      }
    }
    // add edges
    for (Iterator<Cell> cellIt = hexMap.getCellIterator(); cellIt.hasNext(); ) {
      Cell c = cellIt.next();
      if (c.getContent() == Cell.Content.EMPTY) {
        for (Cell.Direction d : Cell.Direction.values()) {
          Cell neighborCell = c.getNeighborCell(d);
          if (neighborCell != null && neighborCell.getContent() == Cell.Content.EMPTY) {
            graph.addKante(graph.getKnoten(c), graph.getKnoten(neighborCell), 1);
          }
        }
      }
    }
  }

  /**
   * Return the current viewing direction.
   */
  public Vector3f getDirection() {
    return getPose().getDirection();
  }

  public Cell getTargetCell() {
    return targetCell;
  }
}
