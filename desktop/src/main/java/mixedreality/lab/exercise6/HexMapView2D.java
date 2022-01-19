package mixedreality.lab.exercise6;

import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import mixedreality.lab.exercise6.avatar.AvatarController;
import mixedreality.lab.exercise6.map.Cell;
import mixedreality.lab.exercise6.map.HexMap;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Iterator;

/**
 * This canvas is able to display a hex-map.
 */
public class HexMapView2D extends JPanel implements MouseListener {

  /**
   * This is the map model
   */
  private HexMap map;

  /**
   * Scaling of the map rendering in 2d to fit to the 2D canvas.
   */
  private float scale;

  /**
   * Controller for of the avatar
   */
  private AvatarController animController;

  public HexMapView2D(int canvasWidth, int canvasHeight, HexMap map) {
    this.map = map;
    this.animController = null;
    setPreferredSize(new Dimension(canvasWidth, canvasHeight));
    setSize(new Dimension(canvasWidth, canvasHeight));

    float scaleX = canvasWidth / map.getGlobalWidth();
    float scaleY = canvasHeight / map.getGlobalHeight();
    scale = Math.min(scaleX, scaleY);
    addMouseListener(this);
  }

  /**
   * Hex map might have changed.
   */
  public void update() {
    repaint();
  }

  @Override
  public void paint(Graphics g) {
    drawMap(g);
    if ( animController != null ) {
      drawAvatar(g, animController.getPose().getPosition());
    }
  }

  private void drawAvatar(Graphics g, Vector3f pos) {
    g.setColor(Color.BLACK);
    int diameter = 8;
    g.fillArc(
            (int) (pos.x * scale)-diameter/2, (int) (pos.z * scale)-diameter/2, diameter, diameter, 0, 360);
  }

  /**
   * Draw the hexmap
   */
  private void drawMap(Graphics g) {
    for (Iterator<Cell> cellIt = map.getCellIterator(); cellIt.hasNext(); ) {
      Cell cell = cellIt.next();
      drawCell(g, cell);
    }
  }

  /**
   * Draw a cell
   */
  private void drawCell(Graphics g, Cell cell) {
    int[] x = new int[6];
    int[] y = new int[6];

    for (int i = 0; i < 6; i++) {
      Vector2f a = cell.getGlobalCorner(i).mult(scale);
      x[i] = (int) a.x;
      y[i] = (int) a.y;
      //g.drawLine(, (int)a.y, (int)b.x, (int)b.y);
    }

    Color color = Color.BLACK;
    switch (cell.getContent()) {
      case TREE -> color = new Color(0.25f, 0.5f, 0.25f);
      case EMPTY -> color = Color.LIGHT_GRAY;
    }
    if (animController != null && cell == animController.getTargetCell()) {
      color = Color.RED;
    }

    g.setColor(color);
    g.fillPolygon(x, y, 6);
    g.setColor(Color.BLACK);
    g.drawPolygon(x, y, 6);
  }

  @Override
  public void mouseClicked(MouseEvent e) {
    Cell selectedCell = map.getClosestCell(new Vector2f(e.getX() / scale, e.getY() / scale));
    if (selectedCell != null && selectedCell.getContent() == Cell.Content.EMPTY && animController != null) {
      animController.setNewTarget(selectedCell);
    }
    repaint();
  }

  @Override
  public void mousePressed(MouseEvent e) {

  }

  @Override
  public void mouseReleased(MouseEvent e) {

  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }

  public void setAnimationController(AvatarController animationController) {
    animController = animationController;
  }
}
