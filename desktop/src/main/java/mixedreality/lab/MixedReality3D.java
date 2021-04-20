package mixedreality.lab;

import com.jme3.app.SimpleApplication;
import com.jme3.system.AppSettings;
import com.jme3.system.JmeCanvasContext;
import mixedreality.lab.base.ui.ComputergraphicsFrame;
import mixedreality.lab.base.ui.ComputergraphicsJMEApp;
import mixedreality.lab.base.ui.Scene3D;
import mixedreality.lab.exercise2.TransformationsScene;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This launcher is used to start the 3D application with a Java Swing user interface.
 */
public class MixedReality3D {

  private static ComputergraphicsJMEApp app;
  private static Scene3D scene;
  private static JmeCanvasContext jmeContext;

  /**
   * Create a context for a JME SimpleApplication.
   */
  public static void createJmeCanvas() {
    AppSettings settings = new AppSettings(true);
    settings.setWidth(400);
    settings.setHeight(400);
    app = new ComputergraphicsJMEApp(scene);
    app.setSettings(settings);
    app.setShowSettings(false);
    app.createCanvas();
    app.setPauseOnLostFocus(false);
    jmeContext = (JmeCanvasContext) app.getContext();
    jmeContext.setSystemListener(app);
  }

  /**
   * Start the JME application.
   */
  public static void startApp() {
    app.startCanvas();
    app.enqueue(() -> {
      if (app instanceof SimpleApplication) {
        app.getFlyByCamera().setDragToRotate(true);
      }
    });
  }

  public static void main(String[] args) {
    // Change scene object here
    scene = new TransformationsScene();
    //scene = new StereoScene();

    createJmeCanvas();
    try {
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    SwingUtilities.invokeLater(() -> {
      ComputergraphicsFrame cgFrame = new ComputergraphicsFrame(jmeContext, scene);
      startApp();
    });
    Logger.getLogger("com.jme3").setLevel(Level.SEVERE);
  }
}
