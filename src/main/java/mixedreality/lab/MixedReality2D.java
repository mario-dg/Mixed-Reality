package mixedreality.lab;

import com.jme3.math.Vector2f;
import mixedreality.lab.base.math.Curve;
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

    // Set the 2D canvas here
    Curve curve = new Curve(new BasisFunctionDummy());
    curve.setControlPoints(new Vector2f(-2, -2), new Vector2f(-1, 2),
            new Vector2f(1, -2), new Vector2f(2, 3));
    Scene2D canvas2D = new CurveScene2D(600, 600, curve);
    //Scene2D canvas2D = new MyRendererScene(640, 480);

    // Layout
    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new BorderLayout());
    getContentPane().add(mainPanel);
    mainPanel.add(canvas2D, BorderLayout.CENTER);
    JPanel ui = canvas2D.getUi();
    if (ui != null) {
      mainPanel.add(ui, BorderLayout.EAST);
    }

    // Window setup
    setTitle("Mixed Reality");
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setSize(800, 600);
    setVisible(true);
  }

  public static void main(String[] args) {
    new MixedReality2D();
  }
}
