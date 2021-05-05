/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise6.map;

import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;

import java.awt.*;

/**
 * A hexagonal cell
 */
public class Cell {

  /**
   * Directions from a cell towards its neighboring cells.
   */
  public enum Direction {
    HOUR_0, HOUR_2, HOUR_4, HOUR_6, HOUR_8, HOUR_10;

    /**
     * Opposite direction of a given direction.
     */
    public Direction getOpposite() {
      return values()[(ordinal() + 3) % 6];
    }
  }

  /**
   * Cell content.
   */
  public enum Content {EMPTY, TREE}


  /**
   * Array with links to neighboring cells
   */
  private Cell[] neighbor;

  /**
   * Cell index in a grid
   */
  private Point index;

  /**
   * Content of the cell.
   */
  private Content content;

  public Cell(Point index, Content content) {
    this.index = new Point(index);
    this.neighbor = new Cell[6];
    this.content = content;
  }

  /**
   * Get the neighboring cell in the given direction. Returns null if the cell has no neighbor in the given direction.
   */
  public Cell getNeighborCell(Direction direction) {
    if (neighbor[direction.ordinal()] == null) {
      return null;
    }
    return neighbor[direction.ordinal()];
  }

  /**
   * Set other cell as neighbor (only used at map generation).
   */
  public void setNeighbor(Direction direction, Cell neighbor) {
    this.neighbor[direction.ordinal()] = neighbor;
    neighbor.neighbor[direction.getOpposite().ordinal()] = this;
  }

  /**
   * Returns the distance to the other cell.
   */
  public float getDistanceTo(Cell other) {
    return getGlobalCenter().distance(other.getGlobalCenter());
  }

  @Override
  public String toString() {
    return String.format("(%d/%d)", index.x, index.y);
  }

  /// GETTER/SETTER

  public Content getContent() {
    return content;
  }

  public Point getIndex() {
    return index;
  }

  /**
   * Compute the world coordinates for a given (cell) index.
   */
  public Vector2f getGlobalCenter() {
    float x = index.x * HexMap.CELL_SIDELENGTH * 1.5f + HexMap.CELL_SIDELENGTH;
    float y = index.y * computeCellHeight() * 2.0f
            + ((index.x % 2 == 1) ? computeCellHeight() : 0)
            + HexMap.CELL_SIDELENGTH;
    return new Vector2f(x, y);
  }

  /**
   * Comute the height of a cell based on the cell side length.
   */
  private float computeCellHeight() {
    return FastMath.sqrt(3.0f) / 2.0f * HexMap.CELL_SIDELENGTH;
  }

  /**
   * Return the 2D local coordinates of the cell corner with the given index.
   */
  public Vector2f getLocalCorner(int cornerIndex) {
    float alpha = cornerIndex / 3.0f * FastMath.PI;
    float x = FastMath.cos(alpha);
    float y = FastMath.sin(alpha);
    return new Vector2f(x, y);
  }


  /**
   * Get the 2D coordinates of the corner in the global map coordinate system.
   */
  public Vector2f getGlobalCorner(int cornerIndex) {
    return getGlobalCenter().add(getLocalCorner(cornerIndex).mult(HexMap.CELL_SIDELENGTH));
  }
}
