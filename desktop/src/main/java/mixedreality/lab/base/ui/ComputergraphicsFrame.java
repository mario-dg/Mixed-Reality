/**
 * Diese Datei ist Teil des Vorgabeframeworks für die Veranstaltung "Mixed Reality"
 *
 * Prof. Dr. Philipp Jenke, Hochschule für Angewandte Wissenschaften Hamburg.
 */

package mixedreality.lab.base.ui;

import com.jme3.system.JmeCanvasContext;
import mixedreality.lab.base.sprites.Constants;
import mixedreality.lab.base.sprites.SpriteCanvas;

import javax.swing.*;
import java.awt.*;

/**
 * This class represents and generates the Java Swing user interface.
 */
public class ComputergraphicsFrame extends JFrame {

  public ComputergraphicsFrame(JmeCanvasContext jmeCanvasContext, Scene3D scene) {
    super(scene.getTitle());

    // JME
    Dimension dim = new Dimension(600, 600);
    Canvas jmeCanvas = jmeCanvasContext.getCanvas();
    jmeCanvas.setPreferredSize(dim);
    jmeCanvas.setMinimumSize(dim);

    // Here be dragons
    SpriteCanvas spriteCanvas = new SpriteCanvas(800, 70);
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

    // Layout
    Box mainPanel = new Box(BoxLayout.X_AXIS);
    getContentPane().add(mainPanel);
    mainPanel.add(jmeCanvas);

    // UI panel
    Box uiPanel = new Box(BoxLayout.Y_AXIS);
    uiPanel.setOpaque(true);
    uiPanel.setBackground(Color.WHITE);
    uiPanel.add(spriteCanvas);
    JPanel sceneUI = scene.getUI();
    if (sceneUI != null) {
      sceneUI.setOpaque(true);
      sceneUI.setBackground(Color.WHITE);
      uiPanel.add(sceneUI);
    }
    uiPanel.add(Box.createHorizontalGlue());

    mainPanel.add(uiPanel);

    setSize(800,600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    //pack();
    setVisible(true);
  }
}
