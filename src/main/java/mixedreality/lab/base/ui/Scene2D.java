/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 * <p>
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.base.ui;

import com.jme3.math.Vector2f;

import javax.swing.*;
import java.awt.*;

/**
 * Base scene canvas class for all 2D implementations.
 */
public class Scene2D extends Canvas {

  protected static final int POINT_SIZE = 6;

  /**
   * Lower left corner in world coordinates.
   */
  private Vector2f ll;

  /**
   * Upper right corner in world coordinates.
   */
  private Vector2f ur;

  /**
   * Last mouse position.
   */
  protected Vector2f lastMousePosition;

  public Scene2D(int width, int height, Vector2f ll, Vector2f ur) {
    setSize(width, height);
    lastMousePosition = null;
    setRenderArea(ll, ur);
  }

  /**
   * Set the displayed render area.
   */
  public void setRenderArea(Vector2f ll, Vector2f ur) {
    this.ll = ll;
    this.ur = ur;
  }

  public Scene2D(int width, int height) {
    this(width, height, new Vector2f(-3, -3), new Vector2f(3, 3));
  }

  /**
   * Draw the scene content.
   */
  public void paint(Graphics g) {
    Graphics2D gc = (Graphics2D) g;
    drawPoint(gc, new Vector2f(1, 1), Color.BLUE);
    drawLine(gc, new Vector2f(-1, 1), new Vector2f(1, -1), Color.BLUE);
  }

  /**
   * Return the used interface for this scene.
   */
  public JPanel getUi() {
    JPanel panel = new JPanel();
    panel.add(new JLabel("no user interface here"));
    return panel;
  }

  /**
   * Draw a line from a to be using the given color.
   */
  protected void drawLine(Graphics gc, Vector2f a, Vector2f b, Color color) {
    Vector2f a_ = world2Pixel(a);
    Vector2f b_ = world2Pixel(b);
    gc.setColor(color);
    gc.drawLine((int) a_.x, (int) a_.y, (int) b_.x, (int) b_.y);
  }

  /**
   * Draw a point at the posisition p using the given color.
   */
  protected void drawPoint(Graphics gc, Vector2f p, Color color) {
    Vector2f pixelPoint = world2Pixel(p);
    int x = (int) pixelPoint.x - POINT_SIZE / 2;
    int y = (int) pixelPoint.y - POINT_SIZE / 2;
    gc.setColor(color);
    gc.fillArc(x, y, POINT_SIZE, POINT_SIZE, 0, 360);
    gc.setColor(Color.BLACK);
    gc.drawArc(x, y, POINT_SIZE, POINT_SIZE, 0, 360);
  }

  /**
   * World -> pixel coordinates.
   */
  protected Vector2f world2Pixel(Vector2f pWorld) {
    return unit2Pixel(world2Unit(pWorld));
  }

  /**
   * Pixel coordinates -> world coordinates.
   */
  protected Vector2f pixel2World(Vector2f pPixel) {
    return unit2World(pixel2Unit(pPixel));
  }

  /**
   * World coordinates -> unit coordinates.
   */
  private Vector2f world2Unit(Vector2f pWorld) {
    Vector2f diagonal = ur.subtract(ll);
    float scale = Math.max(diagonal.x, diagonal.y);
    Vector2f pUnit = pWorld.subtract(ll).mult(1.0f / scale);
    return pUnit;
  }

  /**
   * Unit coordinates -> world coordinates.
   */
  private Vector2f unit2World(Vector2f pUnit) {
    Vector2f diagonal = ur.subtract(ll);
    float scale = Math.max(diagonal.x, diagonal.y);
    return pUnit.mult(scale).add(ll);
  }

  /**
   * Unit coordinates -> pixel coordinates
   */
  private Vector2f unit2Pixel(Vector2f pUnit) {
    float scale = Math.min(getWidth(), getHeight());
    Vector2f offset = new Vector2f((getWidth() - scale) / 2.0f, (getHeight() - scale) / 2.0f);
    Vector2f pPixel = pUnit.mult(scale).add(offset);
    pPixel.y = getHeight() - pPixel.y;
    return pPixel;
  }

  /**
   * Pixel coordinates -> unit coordinates.
   */
  private Vector2f pixel2Unit(Vector2f pPixel) {
    float scale = Math.min(getWidth(), getHeight());
    Vector2f offset = new Vector2f((getWidth() - scale) / 2.0f, (getHeight() - scale) / 2.0f);
    Vector2f pUnit = new Vector2f(pPixel.x, getHeight() - pPixel.y);
    pUnit = pUnit.subtract(offset).mult(1.0f / scale);
    return pUnit;
  }
}
