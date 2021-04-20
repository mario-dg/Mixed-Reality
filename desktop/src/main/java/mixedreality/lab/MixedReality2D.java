package mixedreality.lab;

import com.jme3.math.Vector2f;
import mixedreality.lab.base.math.Curve;
import mixedreality.lab.base.sprites.Constants;
import mixedreality.lab.base.sprites.SpriteCanvas;
import mixedreality.lab.base.ui.Scene2D;
import mixedreality.lab.exercise1.BasisFunctionDummy;
import mixedreality.lab.exercise1.CurveScene2D;

import javax.swing.*;
import java.awt.*;

/**
 * Lecture support application for 2D scenes
 */
public class MixedReality2D extends JFrame {

  public MixedReality2D() {

    // Layout
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    getContentPane().add(mainPanel);

    // Set the 2D canvas here
    Curve curve = new Curve(new BasisFunctionDummy());
    curve.setControlPoints(new Vector2f(-2, -2), new Vector2f(-1, 2),
            new Vector2f(1, -2), new Vector2f(2, 3));
    // TODO: Hier anpassen
    Scene2D canvas2D = new CurveScene2D(600, 600, curve);
    //Scene2D canvas2D = new MyRendererScene(640, 480);
    //Scene2D canvas2D = new SimplificationScene(640, 480);
    mainPanel.add(canvas2D, BorderLayout.CENTER);

    // User interface
    JPanel ui = canvas2D.getUi();
    if (ui != null) {
      mainPanel.add(ui, BorderLayout.EAST);
      ui.setOpaque(true);
      ui.setBackground(Color.WHITE);
    }

    // Here be dragons
    SpriteCanvas spriteCanvas = new SpriteCanvas(100, 70);
    new Thread(() -> {
      while (true) {
        spriteCanvas.loop();
        spriteCanvas.repaint();
        try {
          Thread.sleep((int) (1000.0 / Constants.RENDER_FPS));
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }
    }).start();
    mainPanel.add(spriteCanvas, BorderLayout.SOUTH);

    // Window setup
    setTitle("Mixed Reality");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setSize(800, 600);
    pack();
    setVisible(true);
  }

  public static void main(String[] args) {
    new MixedReality2D();
  }
}
