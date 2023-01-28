package mixedreality;

import com.jme3.system.AppSettings;
import mixedreality.lab.exercise2.TransformationsScene;
import ui.CG3DApplication;

/**
 * This launcher is used to start the 3D application with a Java Swing user interface.
 */
public class MixedReality3D extends CG3DApplication {

  public MixedReality3D() {

    // Asssignment 2
    setScene3D(new TransformationsScene());

    // Assignment 5
    //setScene3D(new StereoScene());

    // Assignment 6
    //setScene3D(new HexMapScene());

    // Assignment 7
    //setScene3D(new VolumeDataScene());
  }

  public static void main(String[] args) {
    // Setup JME app
    var app = new MixedReality3D();
    AppSettings appSettings = new AppSettings(true);
    appSettings.setTitle("Intro to Computer Graphics");
    appSettings.setResolution(800, 600);
    appSettings.setFullscreen(false);
    appSettings.setAudioRenderer(null);
    app.setSettings(appSettings);
    app.setShowSettings(false);
    app.setDisplayStatView(false);
    app.start();
  }
}
