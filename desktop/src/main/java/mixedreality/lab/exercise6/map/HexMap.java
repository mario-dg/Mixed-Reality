/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.exercise6.map;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Represents the map consisting of cells, the controlled character and a target.
 */
public class HexMap {

  /**
   * Side length of a cell.
   */
  public static final float CELL_SIDELENGTH = 1.0f / 2.0f;

  /**
   * Menge aller Zellen
   */
  private Set<Cell> cells = new HashSet<Cell>();

  /**
   * Number of rows in the cell grid
   */
  private int numRows;

  /**
   * Number of rows in the cell grid
   */
  private int numCols;

  public HexMap(int newCols, int numRows) {
    this.numRows = 0;
    this.numCols = 0;
    generateMap(newCols, numRows);
  }

  /**
   * Add a cell, only used during generation.
   */
  public void addCell(Cell zelle) {
    cells.add(zelle);
  }

  /**
   * Return a random unoccupied cell
   */
  public Cell getRandomNonOccupedCell() {
    Cell randomCell = null;
    while (randomCell == null) {
      randomCell = new ArrayList<>(cells).get((int) (Math.random() * cells.size()));
      if (randomCell.getContent() != Cell.Content.EMPTY) {
        randomCell = null;
      }
    }
    return randomCell;
  }

  /**
   * Generate a random map with the given dimensions.
   */
  public void generateMap(int numCols, int numRows) {
    this.numRows = numRows;
    this.numCols = numCols;

    cells.clear();
    Cell[][] cellCache = new Cell[numCols][numRows];

    for (int i = 0; i < numCols; i++) {
      for (int j = 0; j < numRows; j++) {
        Cell zelle = new Cell(new Point(i, j),
                Math.random() < 0.2f ? Cell.Content.TREE : Cell.Content.EMPTY);
        addCell(zelle);
        cellCache[i][j] = zelle;
      }
    }

    for (int i = 0; i < numCols; i++) {
      for (int j = 0; j < numRows; j++) {
        if (i < numCols - 1) {
          if (i % 2 == 0) {
            if (j > 0) {
              cellCache[i][j].setNeighbor(Cell.Direction.HOUR_2,
                      cellCache[i + 1][j - 1]);
            }
            cellCache[i][j].setNeighbor(Cell.Direction.HOUR_4,
                    cellCache[i + 1][j]);

          } else {
            cellCache[i][j].setNeighbor(Cell.Direction.HOUR_2,
                    cellCache[i + 1][j]);
            if (j < numRows - 1) {
              cellCache[i][j].setNeighbor(Cell.Direction.HOUR_4,
                      cellCache[i + 1][j + 1]);
            }
          }
        }
        if (j < numRows - 1) {
          cellCache[i][j].setNeighbor(Cell.Direction.HOUR_6,
                  cellCache[i][j + 1]);
        }
      }
    }
  }

  /**
   * Returns an iterator over all cells.
   */
  public Iterator<Cell> getCellIterator() {
    return cells.iterator();
  }

  public Vector3f getCenter() {
    return new Vector3f(numCols * 1.5f / 2.0f * CELL_SIDELENGTH, 0,
            numRows * 1.5f / 2.0f * CELL_SIDELENGTH);
  }

  /**
   * Width of the map in global coordinates
   */
  public float getGlobalWidth() {
    return (numCols + 0.5f) * CELL_SIDELENGTH * 1.5f;
  }

  /**
   * Height of the map in global coordinates.
   */
  public float getGlobalHeight() {
    return (numRows + 0.5f) * CELL_SIDELENGTH * 1.5f;
  }

  /**
   * Convert a 2D vector to a 3D vector.
   */
  public static Vector3f to3D(Vector2f p) {
    return new Vector3f(p.x, 0, p.y);
  }

  /**
   * Return the cell with the closest center to the given position.
   */
  public Cell getClosestCell(Vector2f pos) {
    Cell selectedCell;
    selectedCell = null;
    float minDist = Float.POSITIVE_INFINITY;
    for (Iterator<Cell> it = getCellIterator(); it.hasNext(); ) {
      Cell c = it.next();
      float dist = c.getGlobalCenter().distance(pos);
      if (selectedCell == null || dist < minDist) {
        selectedCell = c;
        minDist = dist;
      }
    }
    return selectedCell;
  }
}
